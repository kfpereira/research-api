let data;

function consultar() {
  window.location = "consulta.html";
}

function listarDados() {
  let xhr = new XMLHttpRequest();
  xhr.open("GET", "http://localhost:8080/api/dados/estrutura-basica", false);
  const TOKEN = localStorage.getItem("TOKEN");
  let tk = `Bearer ${TOKEN}`;
  xhr.setRequestHeader("Authorization", tk);
  xhr.send(null);
  data = JSON.parse(xhr.response);
  let test = document.getElementById("dados");
  let strBuffer = ` ID Pesquisa: ${data.pesquisa.id}<br> Perguntas e Respostas: <BR>`;

  data.perguntasERespostas.forEach((element) => {
    strBuffer += ` <br><h6>ID: ${element.pergunta.id}</h6>`;
    strBuffer += ` Descrição: <b>${element.pergunta.descricao}</b>`;
    strBuffer += ` Número: ${element.pergunta.numero}`;
    strBuffer += "<br>";

    element.respostas.forEach((elementokk) => {
      strBuffer += ` ID: ${elementokk.id}`;
      strBuffer += ` Descrição: ${elementokk.descricao}`;
      strBuffer += ` Opção: ${elementokk.opcao}`;
      strBuffer += "<br>";
    });
  });

  test.innerHTML = strBuffer;

  console.log(data);
}
