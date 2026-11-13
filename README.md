# Spotify Ultimate — v0.1.0

Projeto Android nativo em Kotlin + Jetpack Compose.

## O que já existe nesta base
- Navegação Home / Search / Your Library / My Editions.
- Alternância visual entre tema Spotify-inspired e Apple-inspired.
- Criação local de edições personalizadas.
- Player compacto ilustrativo, ainda sem conexão com Spotify.

## O que ainda não está ligado
- Login OAuth com Spotify.
- Spotify App Remote e controles reais de reprodução.
- Leitura e escrita de playlists via Web API.
- Capas importadas da galeria e editor completo de tracklist.

Esses recursos serão implementados em etapas próprias. O app não pede nem armazena a senha do Spotify.

## Requisitos de build
- JDK 17
- Android SDK 36
- Gradle 8.13
- Android Gradle Plugin 8.13.2

Abra a pasta no Android Studio ou execute:
`gradle assembleDebug`

O APK de debug será gerado em:
`app/build/outputs/apk/debug/app-debug.apk`

## GitHub Actions
O workflow `.github/workflows/build-apk.yml` compila o APK ao iniciar manualmente uma execução ou ao enviar uma tag `v*`.

## Integração Spotify
Para ativar a integração será necessário registrar um aplicativo no Spotify Developer Dashboard, configurar o package name e a assinatura do APK, e cadastrar um Redirect URI. Não coloque Client Secret em aplicativo Android.
