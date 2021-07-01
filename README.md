## <b>≡ JavaExam</b>

### <b>Краткое описание</b>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
   Это учебный проект по Spring Framework. Используемые технологии: Spring Boot, Spring MVC, Spring Data, Spring Security, Maven, MySQL, Thymeleaf, Flyway, Lombok (также, HTML, CSS, JS).</p>

### <b>Поставленная задача</b><br>
Написать приложение для проведения тестирования по Java. Основные требования:<br>
* Администратор создает учетные записи и выдает пользователям логин и пароль для прохождения тестирования;
* Пользователь, проходящий тестирование, получает доступ к тесту по полученным от администратора учетным данным и может пройти тест только один раз;
* Результаты теста, пройденного пользователем, сохраняются в БД и могут быть просмотрены администратором;
* Результаты теста должны быть представлены в виде диаграммы, отображающей общий результат прохождения теста, а также результат по отдельно взятым темам (в процентах).

Комментарий. При проектировании приложения были выбраны два уровня тем: раздел (напр., Java, Spring) и тема (напр., Java Core, Spring Web). Соответственно, результат в процентах можно просмотреть по каждому отдельно взятому разделу или теме.

### <b>Демо (алгоритм использования приложения)</b><br>

<p style="margin-left: 44px"><b>1. Вход под админом</b></p>

Нажать на иконку

![Вход под админом](https://github.com/aveJava/JavaExam/blob/master/images_for_readme/1.jpg)

Ввести графический ключ:

![Графический ключ](https://github.com/aveJava/JavaExam/blob/master/images_for_readme/2.jpg)

<b>2. Создание учетной записи (сессии)</b><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Для создания учетной записи перейти на страницу 'Создать учетку'<br>

![](https://github.com/aveJava/JavaExam/blob/master/images_for_readme/3.jpg)

<b>2.1. Создание или выбор схемы сессии</b><br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Схема сессии определяет сколько вопросов и по каким темам будет включено в сессию. Схема состоит из разделов, каждый раздел может включать до 10 тем, под каждой темой находится поле для указания количества вопросов по данной теме. Если должно быть включено больше 10 тем по одному разделу, допускается указание этого раздела несколько раз (темы указанные в обоих разделах будут просуммированы).
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
В случае, если вопросов было указано больше, чем их есть в БД по данной теме, цифра будет выделена красным (без обновления страницы). О максимально допустимом количестве вопровов сообщает placeholder поля ввода количества вопросов.

![](https://github.com/aveJava/JavaExam/blob/master/images_for_readme/4.jpg)

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Созданная схема можеть быть сохранена (для этого ей необходимо дать имя) и затем загружена (по присвоенному имени) при создании новой сессии. Также, перед созданием новой сессии в загруженную схему могут быть внесены правки. В этом случае сессия будет создана с учетом внесенных правок, но полученную схему нельзя будет загрузить снова (если, конечно, она не была сохранена перед созданием сессии с присвоением имени).

![](https://github.com/aveJava/JavaExam/blob/master/images_for_readme/5.jpg)

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Схема сессии может быть не только создана, но и загружена из БД.

![](https://github.com/aveJava/JavaExam/blob/master/images_for_readme/6.jpg)
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Все сохраненные с присвоениме имени схемы можно посмотреть нажав кнопку 'Показать сохраненные схемы'.

![](https://github.com/aveJava/JavaExam/blob/master/images_for_readme/7.jpg)

<b>2.2. Создание сессии</b></p>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Для того, чтобы создать сессию по выбранной схеме, достаточно нажать кнопку 'Создать сеанс по данной схеме'. Если схема была заполнена корректно, то будет сгенерирована новая учетная запись, новая сессия и произойдет перенаправление на страницу с учетными данными, которые необходимо передать пользователю для прохождения сессии. Если были допущены ошибки (например, не указано ни одной темы, или указано вопросов больше, чем их есть в БД), то вверху страницы появится окно с сообщениями о допущенных ошибках. 

В случае ошибок в схеме:

![](https://github.com/aveJava/JavaExam/blob/master/images_for_readme/16.jpg)

При правильной схеме:

![](https://github.com/aveJava/JavaExam/blob/master/images_for_readme/8.jpg)

<b>3. Передача логина и пароля пользователю</b><br>
<b>4. Вход пользователя под полученными учетными данными</b><br>

![](https://github.com/aveJava/JavaExam/blob/master/images_for_readme/9.jpg)

Ввод логина и пароля:

![](https://github.com/aveJava/JavaExam/blob/master/images_for_readme/10.jpg)

<b>5. Прохождение теста</b><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
После ввода учетных данных пользователь перенаправляется на страницу тестирования, где ему последовательно предлагаются вопросы в соответствии с выбранной админом схемой сессии. На данный момент каждый вопрос может включать от двух до четырех вариантов ответа, среди которых только один может быть вырным. 

![](https://github.com/aveJava/JavaExam/blob/master/images_for_readme/11.jpg)

<b>6. Просмотр результатов пользователем</b><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
После того, как пользователь ответил на последний вопрос, он будет перенаправлен на страницу результатов:

![](https://github.com/aveJava/JavaExam/blob/master/images_for_readme/12.jpg)

<b>7. Просмотр результатов администратором</b><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Администратор в любое время может просмотреть результаты любой из завершенных сессий. Для это надо перейти на страницу 'Результаты' и выбрать нужную сессию по имени пользователя.

![](https://github.com/aveJava/JavaExam/blob/master/images_for_readme/13.jpg)

![](https://github.com/aveJava/JavaExam/blob/master/images_for_readme/14.jpg)

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Для админа также доступен просмотр ответов, данных пользователем. Можно просмотреть все ответы, нажав кнопку 'Показать вопросы' или просмотреть ответы по какой-либо отдельно взятой теме, для этого надо щелкнуть на индикатор соответствующей темы

![](https://github.com/aveJava/JavaExam/blob/master/images_for_readme/15.jpg)

Просмотр ответов:

![](https://github.com/aveJava/JavaExam/blob/master/images_for_readme/17.jpg)

<b>8. Удаление сессии (опционально)</b><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
После того, как сессия будет пройдена пользователем и просмотрена админом, админ может удалить ее, в случае, если ее результаты больше ненужны.

![](https://github.com/aveJava/JavaExam/blob/master/images_for_readme/18.jpg)
