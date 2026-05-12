import { type ClassValue, clsx } from 'clsx'
import { twMerge } from 'tailwind-merge'

/**
 * `clsx` で結合したクラス名を `tailwind-merge` で衝突解決するヘルパー。
 *
 * @param inputs 可変長のクラス値（文字列・オブジェクト・配列など）
 * @returns マージ済みの単一クラス文字列
 */
export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}
