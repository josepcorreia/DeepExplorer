O modelo da base de dados encontra-se definido no ficheiro 'databaseSQLite.sql'. 

Para criar a base de dados SQLite (terminal unix), introduzir o comando 'sqlite3 db_deep.db < databaseSQLite.sql'.
O nome da base de dados deve ser "db_deep.db".

Para eliminar os dados presentes numa base de dados já existente, utiliza-se o script presente no ficheiro "cleanscript.sql". No terminal, abre-se a base de dados com o comando 'sqlite3 db_deep.db', e depois na consola do sqlite introduz-se '.read cleanscript.sql'.

Após se concluir a extração das co-ocorrências, para criar índices nas tabelas de modo permitir pesquisas mais rápidas, utiliza-se o script presente no ficheiro "indexes_db.sql". No terminal, abre-se a base de dados com o comando 'sqlite3 db_deep.db', e depois na consola do sqlite introduz-se '.read indexes_db.sql'.
