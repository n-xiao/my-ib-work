const CMD_VIEW_ID = "scrollable-cmd-view";
const PX_GAP_BETWEEN_CMDS = 5;

let cmdOptions = [];

function deleteCommandView() {
    let cmdView = document.getElementById(CMD_VIEW_ID);
    if (cmdView)
        cmdView.remove();
}

function prepareCommandView() {
    deleteCommandView();
    let cmdView = document.createElement("div");
    cmdView.setAttribute("id", CMD_VIEW_ID);
    document.body.appendChild(cmdView);
    adjustCommandView();
    cmdOptions = [];
}

function addCommand(name, description) {
    let cmd = createCommand(name, description);
    let cmdView = document.getElementById(CMD_VIEW_ID);

    let cmdHeight = cmd.offsetHeight;
    let cmdViewHeight = cmdView.offsetHeight;
    let cmdViewNewHeight = cmdHeight + cmdViewHeight;
    cmdView.style.height = cmdViewNewHeight + "px";

    cmd.dataset.index = cmdOptions.push(cmd) - 1;
    cmdView.appendChild(cmd);
    adjustCommandView();
}

function createCommand(name, description) {
    let cmd = document.createElement("div");
    cmd.classList.add("cmdUI");

    let cmdName = document.createElement("span");
    cmdName.classList.add("title");
    cmdName.textContent = name;
    cmd.dataset.text = name;

    let cmdDesc = document.createElement("span");
    cmdDesc.classList.add("description");
    cmdDesc.textContent = description;

    cmd.appendChild(cmdName);
    cmd.appendChild(cmdDesc);

    // cmd.addEventListener("mouseover", (event) => {
    //     selectCommand(event.target);
    // });

    return cmd;
}

function adjustCommandView() {
    let cmdView = document.getElementById(CMD_VIEW_ID);
    let cmdBar = document.getElementById("command-bar");

    // positions the cmdView div to be right above input
    let cmdBarHeight = cmdBar.offsetHeight;
    let cmdViewHeight = cmdView.offsetHeight;
    let cmdViewTopOffset = -(PX_GAP_BETWEEN_CMDS + cmdBarHeight + cmdViewHeight) + 5;
    cmdView.style.top = cmdViewTopOffset + "px";

    let bodyWidth = window.innerWidth;
    let cmdBarWidth = cmdBar.offsetWidth;
    let cmdViewLeftOffset = (bodyWidth - cmdBarWidth) / 2;
    cmdView.style.left = cmdViewLeftOffset + "px";

    cmdView.scrollTop = cmdView.scrollHeight; // scrolls to the bottom
}

function resetCommandView() {
    document.getElementById("args-container").remove();
    let cmdBar = document.getElementById("command-bar");
    cmdBar.innerHTML = cmdBar.dataset.hiddenInnerHTML;
    let cmdInput = document.getElementById("cmd-input");
    cmdInput.value = "";
    cmdInput.focus();

    let argHint = document.getElementById("arg-hint-container");
    if (argHint)
        argHint.remove();

    deleteCommandView();
    rosetta.handleCommandInput(cmdInput.value);
}

function adjustInputWidth() {
    const MIN_CH_WIDTH = 8;
    let cmdInput = document.getElementById("cmd-input");
    let inputLen = cmdInput.value.length;
    if (inputLen > MIN_CH_WIDTH) {
        cmdInput.style.width = inputLen + "ch";
    } else if (inputLen == 0) {
        cmdInput.style.width = "fit-content";
    } else {
        cmdInput.style.width = MIN_CH_WIDTH + "ch";
    }
}

function handleCommandSubmitKey(key, selectedIndex) {
    let cmdInput = document.getElementById("cmd-input");
    if (document.activeElement.dataset.ident == "argInput") {
        handleArgsNavKey(key); // handle as an arg related action
        return;
    }

    if (document.activeElement !== cmdInput)
        return;

    if (key == "Tab") {
        commandTabIncrement();
    } else if (key == "Escape") {
        deleteCommandView();
    } else { // space or enter
        let cmdBar = document.getElementById("command-bar");
        cmdBar.dataset.oldInputText = cmdOptions[Number(selectedIndex)].dataset.text;
        cmdBar.dataset.hiddenInnerHTML = cmdBar.innerHTML;
        cmdBar.innerHTML = "";
        deleteCommandView();
        rosetta.populateArgs();
    }
}

function selectCommandAtPos(index) {
    selectCommand(cmdOptions[Number(index)]);
}

function selectCommand(selCmd) {
    cmdOptions.forEach(cmd => {
        cmd.style.background = "#1E1E1E"
    });
    selCmd.style.background = "black";
    rosetta.setSelectedCmdPos(selCmd.dataset.index);
}

function commandTabIncrement() {
    let cmdInput = document.getElementById("cmd-input");
    let currentSelPos = rosetta.getSelectedCmdPos();
    let selCmd = cmdOptions[currentSelPos];

    if (selCmd.dataset.text != cmdInput.value) {
        selectCommand(selCmd);
        autoCompleteCommand();
        return;
    }

    if (currentSelPos < cmdOptions.length - 1) {
        selCmd = cmdOptions[++currentSelPos];
    } else {
        selCmd = cmdOptions[0];
    }

    selectCommand(selCmd);
    autoCompleteCommand();
}

function autoCompleteCommand() {
    let text = cmdOptions[rosetta.getSelectedCmdPos()].dataset.text;
    let cmdInput = document.getElementById("cmd-input");
    cmdInput.value = text;
}