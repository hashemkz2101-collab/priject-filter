# ساخت آنلاین APK با GitHub Actions

این پروژه برای Build آنلاین با Java 17 آماده شده است.

## روش استفاده

1. کل محتویات این پروژه را داخل یک GitHub Repository قرار دهید.
2. وارد تب **Actions** شوید.
3. Workflow با نام **Build Mahroch Android APKs** را انتخاب کنید.
4. روی **Run workflow** بزنید.
5. بعد از پایان موفق Build، در پایین صفحه بخش **Artifacts** دو فایل خواهید داشت:
   - `Mahroch-Client-Debug`
   - `Mahroch-Manager-Debug`

این Workflow از Java 17، Gradle 8.9 و Android SDK API 35 استفاده می‌کند.

نیازی به نصب Android SDK روی کامپیوتر برای Build آنلاین نیست.
