import type { Role } from '@/api'

/** バックエンド `UserResponse`（swagger 未掲載のため手定義）。 */
export type UserResponse = {
  id?: number
  email?: string
  name?: string
  role?: Role
  createdAt?: string
  updatedAt?: string
}

/** バックエンド `UpdateUserRequest`。 */
export type UpdateUserRequest = {
  name?: string
  password?: string
  currentPassword?: string
}
