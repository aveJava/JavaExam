
$(document).ready(function() {
    $('.deleteImg').click(function(){
        let accept = confirm("Точно хотите удалить сессию?");
        if (accept) sessionDelete($(this).attr('id'));
    });
});


async function sessionDelete(id) {
    fetch(`/entities/exam_sessions/${id}`, {method: 'DELETE'}).then(() => location.reload());
}