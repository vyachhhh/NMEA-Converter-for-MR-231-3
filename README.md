# NMEA-Converter-for-MR-231-3
# https://github.com/vyachhhh/NMEA-Converter-for-MR-231-3

Проект предназначен для конвертации сигналов стандарта IEC61162-1:2000 (NMEA-0183) в виде предложений TTM, RSD и VHW (для НРЛС МР-231).

## Модули
/build - модуль, содержащий в себе зависимости
/mr-231 - модуль, содержащий классы для НРЛС МР-231 (Mr231StationType), преобразователя (Mr231Converter), пакет ru.oogis с api для разработки
/mr-231-3 - модуль, содержаший классы для НРЛС МР-231-3 (Mr2313StationType, Mr2313Converter, Mr2313TrackedTargetMessage)
/startApp - модуль, содержащий исполняемый класс App, в котором описаны контрольные примеры
/testModule - тестовый модуль для тестирования классов НРЛС МР-231-3
/javafxApp - модуль JavaFX-приложения

## Запуск JavaFX-приложения с помощью Docker

# XLaunch
1. Установить VcXsrv https://sourceforge.net/projects/vcxsrv/
2. Запустить VcXsrv (XLaunch)
3. Выбрать Multiple windows и оставить Display number = -1
4. Start no client
5. Поставить галочку напротив Disable access control и нажать "Далее"
6. Готово

# Docker
1. Установка образов:
	docker pull mcr.microsoft.mssql:2019-latest
	docker pull maven:3.9.6-amazoncorretto-8-debian
2. Выполнить скрипт startApp.bat