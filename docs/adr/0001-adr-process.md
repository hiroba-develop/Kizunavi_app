# ADR-0001: ADR の運用プロセス

- Status: Accepted
- Date: 2026-04-20
- Deciders: KizuNavi チーム

## Context

テンプレートを複製して複数プロダクトを運用する際、**なぜその技術・構成を選んだか**が文書化されていないと、オンボーディングや技術刷新の判断が困難になる。

## Decision

1. **意思決定の記録場所**として `docs/adr/` を正とする。
2. **フォーマット**は MADR 準拠（Context / Decision / Consequences / Alternatives / References）。
3. **ファイル名**は `NNNN-kebab-case-title.md`（4 桁連番、重複時は番号を空けず連番で追加）。
4. **カテゴリ単位**で ADR をまとめる（技術1つ1つではなく、関連する判断を1文書に集約）。
5. **README のインデックス**を更新し、新規 ADR 追加時は PR でレビューする。
6. 方針変更時は既存 ADR の `Status` を `Deprecated` にし、**新規 ADR で supersede** する（同一ファイルの上書きによる履歴消失を避ける）。

## Consequences

- Positive: 判断の経緯が追いやすく、テンプレート利用者の自律的な拡張がしやすい。
- Negative: ADR のメンテナンスコストが発生する。軽微なライブラリ更新だけでは ADR を更新しない運用ルールを別途決めてもよい。

## Alternatives Considered

- **Wiki のみ**: リポジトリとバージョンが結びつかず、PR レビューと乖離しやすい → 却下。
- **コメントのみ（コード内）**: 横断的なインフラ判断を載せにくい → 却下。

## References

- [docs/adr/README.md](README.md)
- [MADR template](https://adr.github.io/madr/)
