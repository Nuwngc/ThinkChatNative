# ThinkChat Native Android v1

Bản Android native của ThinkChat, **không dùng WebView**. App kết nối trực tiếp tới backend `https://thinkchat.id.vn`.

## Đã tích hợp
- Đăng nhập + giữ session `sid` trong Android SharedPreferences.
- Tự kiểm tra `/api/me` khi mở app.
- Bắt buộc đổi mật khẩu khi backend trả `mustChangePassword`.
- Danh sách hội thoại + cache offline đơn giản.
- Lịch sử tin nhắn + cache offline.
- Gửi text.
- Chọn ảnh từ máy → upload `/api/upload` → gửi ảnh.
- Socket.IO realtime: `message:new`, `typing`, `message:deleted`, `session:ended`.
- Read receipt qua `/api/conversations/:id/read`.
- Thu hồi tin nhắn của chính mình.
- Đổi tên hiển thị.
- Đăng xuất.

## Những thứ chưa dùng trực tiếp
Web Push/VAPID của PWA không được dùng trong native app. Muốn thông báo khi app bị kill nên thêm Firebase Cloud Messaging (FCM) ở backend + Android.

## Build APK
Mở thư mục này bằng Android Studio, sync Gradle, rồi chọn:
`Build > Build APK(s)`.

APK debug:
`app/build/outputs/apk/debug/app-debug.apk`

## GitHub Actions
Workflow `.github/workflows/build-apk.yml` sẽ build APK và upload artifact `ThinkChat-debug-apk`.

## Backend contract đã đối chiếu
Backend source có các endpoint `/api/login`, `/api/me`, `/api/me/password`, `/api/conversations`, `/api/conversations/:id/messages`, `/api/upload`, `/api/conversations/:id/read`, `/api/messages/:id` và Socket.IO authentication bằng cookie `sid`.
