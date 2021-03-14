function httpPost(url, password, username) {
  let xhr = new XMLHttpRequest();
  let obj;
  xhr.open("POST", url, false);
  xhr.setRequestHeader("Content-Type", "application/json");
  obj = { senha: password, usuario: username };
  xhr.send(JSON.stringify(obj));
  let arr = JSON.parse(xhr.responseText);
  console.log(xhr.responseText);
  TOKEN = arr.token;
  localStorage.setItem("TOKEN", TOKEN);
  window.location.href = "main.html";
}
