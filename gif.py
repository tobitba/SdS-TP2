#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import math
from io import BytesIO
from pathlib import Path

import matplotlib.pyplot as plt
import matplotlib.patches as patches
from PIL import Image
import argparse
import sys


def parse_sim_csv(path):
    """
    Lee el CSV 'por bloques' con el formato:
        t
        id;x;y;theta
        id;x;y;theta
        ...
    Devuelve: dict[int, list[tuple(id, x, y, theta)]] ordenado por t.
    """
    frames = {}
    t = None
    with open(path, encoding="utf-8") as f:
        for raw in f:
            line = raw.strip()
            if not line:
                continue
            if ";" not in line:  # línea de tiempo
                try:
                    t = int(line)
                except ValueError:
                    print(f"[WARN] Línea ignorada (no es tiempo ni partícula): {line}", file=sys.stderr)
                    t = None
                    continue
                frames[t] = []
            else:
                if t is None:
                    print(f"[WARN] Partícula sin tiempo previo: {line}", file=sys.stderr)
                    continue
                parts = line.split(";")
                if len(parts) != 4:
                    print(f"[WARN] Línea de partícula inválida: {line}", file=sys.stderr)
                    continue
                pid = int(parts[0])
                # Reemplazo coma decimal por punto
                x = float(parts[1].replace(",", "."))
                y = float(parts[2].replace(",", "."))
                theta = float(parts[3].replace(",", "."))
                frames[t].append((pid, x, y, theta))
    # ordenar por tiempo
    return dict(sorted(frames.items(), key=lambda kv: kv[0]))


def infer_L(frames):
    max_x = max((x for parts in frames.values() for (_, x, _, _) in [p for p in parts]), default=0.0)
    max_y = max((y for parts in frames.values() for (_, _, y, _) in [p for p in parts]), default=0.0)
    L = max(max_x, max_y)
    if L == 0:
        L = 1.0
    # pequeño margen
    return L * 1.02


def render_gif(frames, L, out_path, duration_ms=300, dpi=150, arrow_len=None, dot_ms=None):
    """
    Genera un GIF con un frame por tiempo usando buffer PNG -> PIL (sin tostring_rgb).
    """
    if L is None:
        L = infer_L(frames)

    if arrow_len is None:
        arrow_len = 0.05 * L  # 5% del lado por defecto

    if dot_ms is None:
        # tamaño de marcador relativo a L (ajustable)
        dot_ms = max(4, int(0.12 * (L ** 0.5)))

    images = []

    for t, particles in frames.items():
        fig, ax = plt.subplots(figsize=(5, 5), dpi=dpi)
        ax.set_xlim(0, L)
        ax.set_ylim(0, L)
        ax.set_aspect("equal", adjustable="box")
        ax.set_title(f"Tiempo {t}")

        # borde del cuadrado
        rect = patches.Rectangle((0, 0), L, L, linewidth=1.5, edgecolor="black", facecolor="black")
        ax.add_patch(rect)

        # partículas + flechas de dirección
        for pid, x, y, theta in particles:
            ax.plot(x, y, "o", ms=dot_ms)
            dx = arrow_len * math.cos(theta)
            dy = arrow_len * math.sin(theta)
            ax.arrow(
                x, y, dx, dy,
                head_width=0.015 * L,
                head_length=0.03 * L,
                length_includes_head=True
            )

        # sin ejes para que quede prolijo
        ax.set_xticks([])
        ax.set_yticks([])
        for spine in ("top", "right", "left", "bottom"):
            ax.spines[spine].set_visible(False)

        # Guardar el frame en buffer PNG y abrir con PIL
        buf = BytesIO()
        plt.savefig(buf, format="png", bbox_inches="tight")
        buf.seek(0)
        im = Image.open(buf).convert("RGB")
        images.append(im)
        buf.close()
        plt.close(fig)

    # Guardar GIF
    images[0].save(
        out_path,
        save_all=True,
        append_images=images[1:],
        duration=duration_ms,
        loop=0,
        optimize=False,
        disposal=2,
    )


def main():
    parser = argparse.ArgumentParser(description="Generar GIF de partículas off-lattice")
    parser.add_argument("--csv", required=True, help="Ruta al CSV de la simulación")
    parser.add_argument("--out", default="simulacion.gif", help="Ruta de salida del GIF")
    parser.add_argument("--L", type=float, default=None, help="Lado del cuadrado; si se omite se infiere")
    parser.add_argument("--ms", type=int, default=300, help="Duración por frame en milisegundos")
    parser.add_argument("--dpi", type=int, default=150, help="DPI para la figura")
    parser.add_argument("--arrow", type=float, default=None, help="Largo de flecha en unidades del sistema")
    args = parser.parse_args()

    frames = parse_sim_csv(args.csv)
    if not frames:
        print("[ERROR] No se pudieron leer frames del CSV.", file=sys.stderr)
        sys.exit(1)

    out_path = Path(args.out)
    render_gif(
        frames=frames,
        L=args.L,
        out_path=out_path,
        duration_ms=args.ms,
        dpi=args.dpi,
        arrow_len=args.arrow,
    )
    print(f"✅ GIF generado en: {out_path.resolve()}")


if __name__ == "__main__":
    main()
