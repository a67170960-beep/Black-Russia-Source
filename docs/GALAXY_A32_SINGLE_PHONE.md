# Один Samsung Galaxy A32: Termux + игра

## Схема

На одном телефоне одновременно работают:

- Termux — сервер;
- Android-приложение — клиент.

Хотспот, второй телефон и публичный IP не нужны. Сервер слушает только 127.0.0.1:7777.

## Подготовка Termux

    pkg update -y
    pkg install -y git clang cmake ninja tmux proot-distro
    termux-wake-lock

Для серверных зависимостей можно использовать Debian через proot:

    proot-distro install debian
    proot-distro login debian
    apt update
    apt install -y git clang cmake ninja-build build-essential

## Запуск

Серверная папка из форка:

    git clone https://github.com/a67170960-beep/Black-Russia-Source.git
    cd Black-Russia-Source/openmp-server

Конфигурация уже использует bind 127.0.0.1 и порт 7777. После запуска сервера проверь:

    ss -lun | grep 7777

Не закрывай Termux и включи блокировку сна командой termux-wake-lock, иначе Android может остановить процесс в фоне.

## Клиент

Для одного телефона в Jni source/jni/clientlogic/CNetwork.cpp нужен адрес:

    CSetServer::create("127.0.0.1", 128, 9, 7777, false)

Обе записи серверов нужно изменить одинаково. После этого требуется пересобрать JNI и APK. Обычный APK из исходного репозитория всё ещё указывает на старый сервер.

## Ограничения A32

Galaxy A32 подходит для лёгкого локального теста, но сборка полного open.mp прямо на телефоне может быть тяжёлой по времени, памяти и свободному месту. Готовый официальный Linux-релиз open.mp под ARM64 не предоставляется, поэтому сервер придётся собирать из исходников либо использовать ARM64-сборку.

Даже после запуска сервера текущий Android-клиент может не пройти модифицированный RakNet/SA-MP handshake. Тогда проблема будет в совместимости протокола, а не в Termux или адресе 127.0.0.1.
