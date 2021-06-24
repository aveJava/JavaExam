
$(document).ready(function () {
    // проставить плэйсхолдеры для выбранных тем при загрузке страницы
    $('.topicSelect')
        .filter(function(i, elem) {
            return $(elem).find('option:selected').text() !== "";
        })
        .each(function(index, elem) {
            process(elem);
        });

    // автоматически менять плейсхолдеры при изменении топиков
    $('.topicSelect').change(function() {
        process(this);
    });

    // при нажатии любой клавиши проверять цвет полей для количества вопросов
    $('.quesCount')
        .filter(function(i, elem) {return $(elem).val() != null})
        .on('input keyup', function (e) {
        checkColor(this);
    });
});






// изменяет поле для ввода количества вопросов
async function process(topic) {
    // элементы документа
    const div = $(topic).parent();
    const questionNumberInput = getQuestionNumberInputForThisTopic(topic);

    // значения элементов
    const foKnVal = $(topic).closest('.unit').children('.UnitFoKn').first().val();
    const topicVal = $(topic).val();

    getCount(foKnVal, topicVal)
            .then((count) => {
                setPlaceholderAndMax(questionNumberInput, count);
                checkColor(questionNumberInput)
            })
}

// получить поле ввода количества вопросов, соответствующее указанному полю выбора темы
function getQuestionNumberInputForThisTopic(topic) {
    const div = $(topic).parent();
    return $(div).children('input[type=number]');
}

// запрос количества вопросов в БД по выбранной теме
async function getCount(foKn, topic) {
    let response = await fetch(`/entities/exam_questions/count?foKn=${foKn}&topic=${topic}`);
    if (response.ok) {
        return response.text();
    } else {
        alert('ошибка при выполнении запроса');
    }
}

// проставить placeholder и max для указанного input
function setPlaceholderAndMax(input, count) {
    $(input).attr('placeholder', `max ${count}`);
    $(input).attr('max', count);
}

// делает поле ввода количества вопросов красным, если значение превышает максимум
function checkColor(input) {
    //alert(`${$(input).val()} - ${$(input).attr('max')} : ${$(input).val() > Number($(input).attr('max'))}`);
    if (Number($(input).val()) > Number($(input).attr('max'))) $(input).css('color', 'red');
    else $(input).css('color', 'black');
}