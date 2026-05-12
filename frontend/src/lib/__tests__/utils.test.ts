import { describe, expect, it } from 'vitest'
import { cn } from '@/lib/utils'

describe('cn', () => {
  it('複数クラスを結合できる', () => {
    expect(cn('px-4', 'py-2')).toBe('px-4 py-2')
  })

  it('Tailwind の競合クラスは後勝ちでマージされる', () => {
    expect(cn('px-2 py-2', 'px-4')).toBe('py-2 px-4')
  })
})
