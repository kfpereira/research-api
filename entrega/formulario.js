function abrirForm() {
  window.location = "formulario.html";
}

function gerarForm() {
  let xhr = new XMLHttpRequest();
  xhr.open("GET", "http://localhost:8080/api/dados/estrutura-basica", false);
  const TOKEN = localStorage.getItem("TOKEN");
  let tk = `Bearer ${TOKEN}`;
  xhr.setRequestHeader("Authorization", tk);
  xhr.send(null);
  data = JSON.parse(xhr.response);
  let test = document.getElementById("divForm");
  let strBuffer = "<h2>Pesquisa </h2>";

  data.perguntasERespostas.forEach((element) => {
    strBuffer += `<br> <div id="${element.pergunta.numero}"><b>Pergunta: ${element.pergunta.descricao}</b></div>`;

    element.respostas.forEach((elementokk) => {
      strBuffer += `<label class="form-check-label" for="opcao${elementokk.id}"> ${elementokk.descricao}</label> 
      <input required checked class="form-check-input" type="radio" name="${element.pergunta.numero}" id="${elementokk.opcao}"></div><br>`;
    });
  });

  test.innerHTML = strBuffer;
  localStorage.setItem("folhaNum", "1");
  console.log(data);
}

function enviarForm(event) {
  let obj = [];
  event.preventDefault();
  let tempOpt,
    radioInput,
    folha = localStorage.getItem("folhaNum");
  data.perguntasERespostas.forEach((element, index) => {
    radioInput = document.getElementsByName(`${element.pergunta.numero}`);
    for (let i = 0; i < radioInput.length; i++) {
      if (radioInput[i].checked) {
        tempOpt = radioInput[i].id;
      }
    }
    obj.push({
      folha: +folha,
      opcaoResposta: `${tempOpt}`,
      pergunta: document.getElementById(`${index + 1}`).id,
      pesquisa: data.pesquisa.id,
    });
  });
  console.log(obj);
  folha++;
  localStorage.setItem("folhaNum", folha);

  let xhr = new XMLHttpRequest();
  xhr.open(
    "POST",
    "http://localhost:8080/api/dados/enviar-entrevistados",
    false
  );
  xhr.setRequestHeader("Content-Type", "application/json");
  const TOKEN = localStorage.getItem("TOKEN");
  let tk = `Bearer ${TOKEN}`;
  xhr.setRequestHeader("Authorization", tk);
  obj = JSON.stringify(obj);
  xhr.send(obj);
}
