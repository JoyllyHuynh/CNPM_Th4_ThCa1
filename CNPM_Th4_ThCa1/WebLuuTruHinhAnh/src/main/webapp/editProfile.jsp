<form action="${pageContext.request.contextPath}/UpdateProfile"
      method="post">

    <div class="form-group">
        <label>Họ và tên</label>
        <input type="text"
               name="fullName"
               value="${sessionScope.user.fullName}"
               required>
    </div>

    <div class="form-group">
        <label>Email</label>
        <input type="email"
               name="email"
               value="${sessionScope.user.email}"
               required>
    </div>

    <button type="submit">
        Lưu thay đổi
    </button>

</form>