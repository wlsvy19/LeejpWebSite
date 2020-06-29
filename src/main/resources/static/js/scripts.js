//댓글 ajax처리
$(".answer-write input[type=submit]").click(addAnswer);
function addAnswer(e) {
	e.preventDefault(); // 서버로 전달되지 않도록 막음
	var queryString = $(".answer-write").serialize();
	var url = $(".answer-write").attr("action");
	console.log("url: " + url);

	$.ajax({
		type : 'post',
		url : url,
		data : queryString,
		dataType : 'json',
		error : function(xhr, status) {
			console.log("add answer error");
		},
		success : onSuccess
	});//end ajax
}

// 댓글 달기
function onSuccess(data, status) {
	console.log("댓글달기 성공");
	console.log(data.valid);
	var answerTemplate = $("#answerTemplate").html();
	var template = answerTemplate.format(data.writer.userId, data.formattedCreateDate, data.contents, data.question.id, data.id);
	$(".qna-comment-slipp-articles").prepend(template); // 댓글 뒤에 붙여주기

	$("textarea[name=contents]").val(""); // textarea 초기화
	
}

// $(".link-delete-answer").click(deleteAnswer);
$(document).on('click', '.link-delete-answer', deleteAnswer); // 댓글달고 바로 삭제
// 안되서 처리
// 댓글 삭제
function deleteAnswer(e) {
	console.log('answer delete button click');
	e.preventDefault();

	var deleteBtn = $(this);
	var url = deleteBtn.attr("href"); // this는 여기서 click 이벤트
	console.log("url: " + url);

	$.ajax({
		type : 'delete',
		url : url,
		dataType : 'json',
		error : function(xhr, status) {
			console.log("delete answer error");
		},
		success : function(data, status) {
			if (data.valid) { // 권한이 있는 댓글 삭제일 경우
				deleteBtn.closest("article").remove(); // 가장 가까이 있는 article 삭제
			} else {
				alert(data.errorMessage);
			}
		}
	});//end ajax
}

//댓글 수정
/*$(document).on('click', '.link-update-answer', updateAnswer); 
function updateAnswer(e) {
	e.preventDefault();
	console.log("댓글수정 클릭");
	
	var updateBtn = $(this);
	var url = updateBtn.attr("href");
	console.log("url: " + url);
	
	$.ajax({
		type : 'put',
		url : url,
		dataType : 'json',
		error : function(xhr, status){
			console.log("댓글 수정중 에러");
		},
		success : function(data, status){
			if(data.valid){
				var reply = $('#reply').text();
				console.log(reply);
			} else{
				alert(data.errorMessage);
			}
		}
	});//end ajax
}*/



// show.html 아래쪽 ajax로 추가되는 템플릿 사용 선언
String.prototype.format = function() {
	var args = arguments;
	return this.replace(/{(\d+)}/g, function(match, number) {
		return typeof args[number] != 'undefined' ? args[number] : match;
	});
};