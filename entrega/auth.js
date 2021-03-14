function autorizar() {
  var xhr = new XMLHttpRequest();
  xhr.open("POST", "http://localhost:8080/api/dados/initDB", false);
  const TOKEN = localStorage.getItem("TOKEN");
  var tk = `Bearer ${TOKEN}`;
  xhr.setRequestHeader("Authorization", tk);
  xhr.send(null);
  console.log(tk);
}
