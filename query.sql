SELECT categoria.content Categoria, dado.content 'Nome do app', usuario.content 'Usuário', senha.content 'Senha'
FROM Data dado
     INNER JOIN Registry senha ON senha.data = dado.id
     INNER JOIN Registry usuario ON usuario.data = dado.id
     INNER JOIN Type tipo ON tipo.id = dado.type
     INNER JOIN Data categoria ON categoria.id = dado.category
WHERE tipo.name = 'Data' 
      AND dado.name = 'NameApp'
      AND senha.name = 'PasswordApp'
      AND usuario.name = 'UserApp'