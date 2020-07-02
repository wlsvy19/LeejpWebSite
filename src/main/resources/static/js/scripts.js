//회원가입 처리
$(".user-create button[type=submit]").click(userCreate);
function userCreate(e) {
	var userId = $('input[name=userId]').val();
	var userPassword = $('input[name=userPassword]').val();
	var userName = $('input[name=userName]').val();
	var userEmail = $('input[name=userEmail]').val();

	if (userId == "") {
		alert("ID를 입력하세요.");
		return false;
	}
	else if (userPassword == "") {
		alert("패스워드를 입력하세요.");
		return false;
	}
	else if (userName == "") {
		alert("이름을 입력하세요.");
		return false;
	}
	else if (userEmail == "") {
		alert("이메일을 입력하세요.");
		return false;
	}
	else
		alert("회원가입이 완료되었습니다.");
}

//사용자 목록
$('.user-list').click(function(e){
	console.log("${userList}");
	return false;
})


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
		error : onError,
		success : onSuccess
	});
}
function onError() {
}

// 댓글 달기
function onSuccess(data, status) {
	console.log("success");
	console.log(data.valid);
	var answerTemplate = $("#answerTemplate").html();
	var template = answerTemplate.format(data.writer.userId,
			data.formattedCreateDate, data.contents, data.question.id, data.id);
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
			console.log("delete error");
		},
		success : function(data, status) {
			if (data.valid) { // 권한이 있는 댓글 삭제일 경우
				deleteBtn.closest("article").remove(); // 가장 가까이 있는 article 삭제
			} else {
				alert("다름 사람의 댓글은 삭제할 수 없습니다.");
			}
		}
	});//end ajax  
}

$(document).on('click', '.link-update-answer', updateAnswer);

function updateAnswer() {

	e.preventDefault();
	console.log("수정 버튼 클릭")
	var a = '';
	a += ' <div class="form-group" style="padding:14px;">';
	a += '<textarea class="form-control" placeholder="댓글을 입력해 주세요" name="contents"></textarea>';
	a += ' </div>';
	a += ' <input type ="submit" class="btn btn-success pull-right" value="댓글 달기" />';

	$('.article-header-text').html(a);
}
function updateAnswer(e) {
	e.preventDefault();

}

// show.html 아래쪽 ajax로 추가되는 템플릿 사용 선언
String.prototype.format = function() {
	var args = arguments;
	return this.replace(/{(\d+)}/g, function(match, number) {
		return typeof args[number] != 'undefined' ? args[number] : match;
	});
};