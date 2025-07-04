**Важно**:
1. Обязательно нужен установленый docker desktop, чтобы собрать docker образ
2. Изначально создаются таблицы roles, users, files
3. roles заполняется двумя ролями user и admin
4. users заполняется одним аккаунтом admin с данными **login:** admin@admin.ru **password:** admin
5. files остаётся пустой таблицей, пока не будут загружены файлы
6. Проверьте, чтобы у вас не было запушено другого postgresql на порте 5432 (проверить можно через cmd командой - **netstat -aon | findstr "5432"**)

📋Инструкция:
1. Запустить docker desktop
2. Перейти в корень с проектом
![image](https://github.com/user-attachments/assets/7c643090-e926-4fcd-8847-9b601b8be3de)
3. Набрать в пути cmd
![image](https://github.com/user-attachments/assets/38beeee5-17ed-4a0e-bf17-41dee1673d50)
4. В консоли набрать команду **docker-compose up --build**
5. После этого будет запущен docker контейнер с postgres и приложением внутри

📏Пути запросов:
**localhost:8080** - основная ссылка, которая переведёт на регистрацию
**localhost:8080/api/register** - ссылка регистрации
**localhost:8080/api/login** - ссылка для входа
**localhost:8080/api/file** - ссылка для выбора файла и создания ссылки
**localhost:8080/api/file/download?id=ID_FROM_FILE** - ссылка для скачивания файла
**localhost:8080/api/admin** - ссылка для админов, для просмотра статистики по файлам
**localhost:8080/api/admin/files** - вспомогательный endpoint для отрисовки статистики

✅Незащищённые endpoints
**localhost:8080/api/register**
**localhost:8080/api/login**

🟡endpoints доступные users и admin
**localhost:8080/api/file**
**localhost:8080/api/file/download?id=ID_FROM_FILE**

🔴endpoint доступные только admin
**localhost:8080/api/admin**
**localhost:8080/api/admin/files**
