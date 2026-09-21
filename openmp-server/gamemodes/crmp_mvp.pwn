#include <open.mp>

#define DIALOG_AUTH 1000
#define COLOR_WHITE 0xFFFFFFFF
#define COLOR_GREEN 0x55FF55FF
#define COLOR_RED 0xFF5555FF
#define COLOR_GREY 0xAAAAAAFF

new bool:g_hasAccount[MAX_PLAYERS];
new bool:g_loggedIn[MAX_PLAYERS];
new g_password[MAX_PLAYERS][65];
new g_money[MAX_PLAYERS];

stock ShowAuthDialog(playerid)
{
    ShowPlayerDialog(
        playerid,
        DIALOG_AUTH,
        DIALOG_STYLE_INPUT,
        "Свой CRMP — вход",
        "Введите пароль. Если аккаунта ещё нет, нажмите кнопку Регистрация.",
        "Войти",
        "Регистрация"
    );
    return 1;
}

stock SpawnAtBase(playerid)
{
    SetPlayerInterior(playerid, 0);
    SetPlayerVirtualWorld(playerid, 0);
    SetPlayerPos(playerid, 1685.4, -2244.5, 13.5);
    SetPlayerFacingAngle(playerid, 90.0);
    SetCameraBehindPlayer(playerid);
    return 1;
}

main() {}

public OnGameModeInit()
{
    SetGameModeText("Own CRMP MVP");
    AddPlayerClass(0, 1685.4, -2244.5, 13.5, 90.0, 0, 0, 0, 0, 0, 0);
    ShowNameTags(true);
    UsePlayerPedAnims();
    SendRconCommand("language Russian");
    return 1;
}

public OnGameModeExit()
{
    return 1;
}

public OnPlayerConnect(playerid)
{
    g_hasAccount[playerid] = false;
    g_loggedIn[playerid] = false;
    g_password[playerid][0] = '\0';
    g_money[playerid] = 5000;
    SendClientMessage(playerid, COLOR_WHITE, "Добро пожаловать на собственный тестовый сервер.");
    SendClientMessage(playerid, COLOR_GREY, "Это оригинальный MVP: данные пока хранятся только до перезапуска.");
    ShowAuthDialog(playerid);
    return 1;
}

public OnPlayerDisconnect(playerid, reason)
{
    g_hasAccount[playerid] = false;
    g_loggedIn[playerid] = false;
    g_password[playerid][0] = '\0';
    return 1;
}

public OnDialogResponse(playerid, dialogid, response, listitem, inputtext[])
{
    if (dialogid != DIALOG_AUTH)
    {
        return 0;
    }

    if (strlen(inputtext) < 4)
    {
        SendClientMessage(playerid, COLOR_RED, "Пароль должен содержать минимум 4 символа.");
        ShowAuthDialog(playerid);
        return 1;
    }

    if (!response)
    {
        if (g_hasAccount[playerid])
        {
            SendClientMessage(playerid, COLOR_RED, "Аккаунт для этого подключения уже зарегистрирован.");
            ShowAuthDialog(playerid);
            return 1;
        }

        format(g_password[playerid], sizeof(g_password[]), "%s", inputtext);
        g_hasAccount[playerid] = true;
        g_loggedIn[playerid] = true;
        SendClientMessage(playerid, COLOR_GREEN, "Регистрация завершена. Используйте /help.");
        SpawnPlayer(playerid);
        return 1;
    }

    if (!g_hasAccount[playerid])
    {
        SendClientMessage(playerid, COLOR_RED, "Аккаунт ещё не зарегистрирован. Нажмите Регистрация.");
        ShowAuthDialog(playerid);
        return 1;
    }

    if (strcmp(g_password[playerid], inputtext, false) != 0)
    {
        SendClientMessage(playerid, COLOR_RED, "Неверный пароль.");
        ShowAuthDialog(playerid);
        return 1;
    }

    g_loggedIn[playerid] = true;
    SendClientMessage(playerid, COLOR_GREEN, "Вход выполнен. Используйте /help.");
    SpawnPlayer(playerid);
    return 1;
}

public OnPlayerSpawn(playerid)
{
    if (!g_loggedIn[playerid])
    {
        return 1;
    }

    ResetPlayerMoney(playerid);
    GivePlayerMoney(playerid, g_money[playerid]);
    SpawnAtBase(playerid);
    return 1;
}

public OnPlayerText(playerid, text[])
{
    if (!g_loggedIn[playerid])
    {
        SendClientMessage(playerid, COLOR_RED, "Сначала войдите в аккаунт.");
        return 0;
    }

    new name[MAX_PLAYER_NAME + 1];
    new message[144];
    GetPlayerName(playerid, name, sizeof(name));
    format(message, sizeof(message), "[Чат] %s: %s", name, text);
    SendClientMessageToAll(COLOR_WHITE, message);
    return 0;
}

public OnPlayerCommandText(playerid, cmdtext[])
{
    if (!g_loggedIn[playerid])
    {
        SendClientMessage(playerid, COLOR_RED, "Сначала войдите в аккаунт.");
        return 1;
    }

    if (strcmp(cmdtext, "/help", true) == 0)
    {
        SendClientMessage(playerid, COLOR_WHITE, "/stats — профиль; /spawn — вернуться на старт; /save — тестовое сохранение.");
        return 1;
    }

    if (strcmp(cmdtext, "/stats", true) == 0)
    {
        new name[MAX_PLAYER_NAME + 1];
        new message[128];
        GetPlayerName(playerid, name, sizeof(name));
        format(message, sizeof(message), "Игрок: %s | Деньги: %d | Состояние: авторизован", name, g_money[playerid]);
        SendClientMessage(playerid, COLOR_WHITE, message);
        return 1;
    }

    if (strcmp(cmdtext, "/spawn", true) == 0)
    {
        SpawnPlayer(playerid);
        return 1;
    }

    if (strcmp(cmdtext, "/save", true) == 0)
    {
        g_money[playerid] = GetPlayerMoney(playerid);
        SendClientMessage(playerid, COLOR_GREEN, "Состояние сохранено в памяти процесса. SQLite добавим следующим шагом.");
        return 1;
    }

    return 0;
}
