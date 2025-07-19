# 🧠 ActiveMind

**ActiveMind** es una aplicación Android educativa que utiliza tarjetas NFC para reforzar el aprendizaje de figuras y colores mediante reconocimiento visual, retroalimentación auditiva y seguimiento de aciertos y errores.

---

## 🚀 Características

- 📱 Interfaz amigable con tarjetas visuales
- 🎯 Reconocimiento NFC con validación de figura y color
- ✅ Contador de aciertos y errores en tiempo real
- ⏳ Ventana modal con cuenta regresiva y feedback visual
- 🔊 Sonido y animaciones para tarjetas correctas e incorrectas
- 🎬 Splash Screen animado al iniciar la app
- 🌓 Soporte para modo claro y oscuro

---

## 📲 Instalación

1. Clona el repositorio:

```bash
git clone https://github.com/Alness1314/Activemind.git
```

2. Ábrelo en **Android Studio Hedgehog o superior**.
3. Conecta un dispositivo físico con NFC (o usa un emulador compatible).
4. Ejecuta la app.

---

## 🧩 Requisitos

- Android 12 (API 31) o superior
- Soporte NFC (NXP NTAG213/215/216)
- Kotlin + Material 3
- Glide, MediaPlayer, RecyclerView

---

## 🔧 Funcionalidad NFC

Cada tarjeta física contiene un payload tipo `NDEF` con un JSON como:

```json
{
  "figure": "Círculo",
  "color": "Rojo"
}
```

Al acercarse al dispositivo, si coincide con la tarjeta seleccionada en pantalla, se valida como correcta.

---

## 📁 Estructura del proyecto

```
├── adapters/
│   └── NfcCardAdapter.kt
├── dto/
│   └── NfcCard.kt
├── interfaces/
│   └── OnClickListener.kt
├── ui/
│   ├── MainActivity.kt
│   └── NfcCardDialogFragment.kt
├── res/
│   ├── layout/ (layouts xml)
│   ├── drawable/ (figuras)
│   └── raw/ (sonidos correct/wrong)
```

---

## 🤝 Contribuciones

¡Pull Requests y mejoras son bienvenidas!

1. Haz un fork
2. Crea tu rama `git checkout -b feature/nueva-funcionalidad`
3. Haz commit `git commit -am 'Agrega nueva funcionalidad'`
4. Push y crea el PR

---

## 🧑‍💻 Autor

**Alness Zadro**  
[![GitHub](https://img.shields.io/badge/GitHub-Alness1314-black?logo=github)](https://github.com/Alness1314)

---

## 📄 Licencia

Este proyecto está bajo la licencia MIT. Consulta el archivo [LICENSE](LICENSE) para más detalles.