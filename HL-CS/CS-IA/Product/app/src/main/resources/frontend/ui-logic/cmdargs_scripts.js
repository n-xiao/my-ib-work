const FOCUS_COLOUR = "rgb(127,74,192)";
const BLUR_COLOUR = "rgb(68, 54, 160)";
const ERROR_COLOUR = "rgb(255,0,0)";

let cmdBar = document.getElementById("command-bar");
let argInputs = [];
let selectedArg = 0;

// handling args

function initArgsContainer() {
    let argsContainer = document.getElementById("args-container");

    if (argsContainer)
        return;

    argsContainer = document.createElement("div");
    argsContainer.id = "args-container";

    argsContainer.style.left = 10 + "px";
    argsContainer.style.width = cmdBar.clientWidth - 10 + "px";

    cmdBar.appendChild(argsContainer);
}

function createArg(nameContent, index, required) {
    let numInput = false; // this is necessary because only string args can be passed by java
    if (nameContent == "Duration")
        numInput = true;

    let argsContainer = document.getElementById("args-container");
    let argBox = document.createElement("div");
    let argName = document.createElement("span");
    let argNameBox = document.createElement("div");
    let argInput = document.createElement("input");

    argBox.classList.add("arg-box");
    argName.classList.add("arg-name");
    argNameBox.classList.add("arg-name-container");
    argInput.classList.add("arg-input");

    argName.textContent = nameContent + ": ";
    argNameBox.appendChild(argName);
    argBox.appendChild(argNameBox);

    initLengthLimitToInput(argInput, numInput);
    initSelectedListenerToInput(argInput);

    argInputs[Number(index)] = argInput;
    argInput.type = "text";
    argInput.dataset.index = Number(index);
    argInput.dataset.ident = "argInput";
    argInput.dataset.required = required == "true";

    argBox.appendChild(argInput);

    argsContainer.appendChild(argBox);
}

function initLengthLimitToInput(input, numInput) {
    let maxLen = 15;

    if (numInput)
        maxLen = 9; // prevent max val abuse

    input.maxLength = maxLen;

    input.addEventListener("input", (_) => {
        let inputLen = input.value.length;
        input.style.width = inputLen + "ch";
    });
}

function resetArgInputs() {
    argInputs = [];
}

function handleArgsNavKey(key) {
    if (key == "Enter") {
        selectedArg = 0;
        inputVals = [];
        argInputs.forEach(input => {
            inputVals.push(input.value.trim());
        });
        rosetta.executeCommand(inputVals);
    } else if (key == "Tab") {
        incrementSelectedArg();
    } else if (key == "Escape") { // restore old input state
        selectedArg = 0;
        inputVals = [];
        argInputs = [];
        resetCommandView();
        let cmdInput = document.getElementById("cmd-input");
        cmdInput.value = cmdBar.dataset.oldInputText;
        rosetta.handleCommandInput(cmdInput.value);
    }
}

function initSelectedListenerToInput(input) {
    input.addEventListener("blur", (event) => {
        let selectedArgBox = event.target.parentElement;
        selectedArgBox.style.borderColor = BLUR_COLOUR;

        let argHint = document.getElementById("arg-hint-container");
        argHint.remove();
    });

    input.addEventListener("focus", (event) => showArgumentHint(event));
    input.addEventListener("keydown", (event) => showArgumentHint(event));

    function showArgumentHint(event) {
        let selectedArgBox = event.target.parentElement;
        selectedArgBox.style.borderColor = FOCUS_COLOUR;
        rosetta.requestArgHint(event.target.dataset.index);
        if (input.textContent.length == 1)
            input.textContent = input.textContent.trim();
    }
}

function incrementSelectedArg() {
    if (selectedArg < argInputs.length - 1)
        selectedArg++;
    else
        selectedArg = 0;

    updateSelectedArg();
}

function updateSelectedArg() {
    argInputs.forEach(input => {
        input.value = input.value.trimStart();
        input.blur();
    });
    let selectedInput = argInputs[selectedArg];
    selectedInput.focus();
    selectedInput.value = selectedInput.value.trim();
}

function setArgAsInvalid(strIndex) {
    let index = Number(strIndex);
    argInputs[index].parentElement.style.borderColor = ERROR_COLOUR;
}

function setArgAsNormal(strIndex) {
    let index = Number(strIndex);
    argInputs[index].parentElement.style.borderColor = BLUR_COLOUR;
}

// handling arg hints
const ARG_HINT_CONTAINER_ID = "arg-hint-container";
const ARG_HINT_TITLE_ID = "arg-hint-title";
const ARG_HINT_BODY_ID = "arg-hint-body";

function showArgHint(argTitle, argBody) {
    let argHint = document.getElementById(ARG_HINT_CONTAINER_ID);

    if (!argHint) {
        argHint = document.createElement("div");
        argHint.id = ARG_HINT_CONTAINER_ID
        let leftOffset = cmdBar.getBoundingClientRect().x;
        let topOffset = cmdBar.getBoundingClientRect().y - argHint.getBoundingClientRect().height - 25;
        argHint.style.left = leftOffset + "px";
        argHint.style.top = topOffset + "px";
        argHint.style.width = cmdBar.getBoundingClientRect().width - 5 + "px";

        document.body.appendChild(argHint);
    }

    argHint.innerHTML = "";

    let argHintTitle = document.createElement("span");
    argHintTitle.id = ARG_HINT_TITLE_ID;
    argHintTitle.textContent = argTitle;
    argHint.appendChild(argHintTitle);

    let argHintBody = document.createElement("span");
    argHintBody.id = ARG_HINT_BODY_ID;
    argHintBody.textContent = argBody;
    argHint.appendChild(argHintBody);
}

function showError(errorMsg) {
    showArgHint("", "");
    let title = document.getElementById(ARG_HINT_TITLE_ID);
    let body = document.getElementById(ARG_HINT_BODY_ID);

    title.textContent = "error"
    body.textContent = errorMsg;

    body.style.color = "red";
}