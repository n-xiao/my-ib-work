function addTask(titleText, timeStart, timeEnd, statusText) {
    let taskUI = document.createElement("div");
    taskUI.classList.add("taskUI");

    let title = document.createElement("p");
    title.classList.add("title");
    title.textContent = titleText;
    taskUI.appendChild(title);

    let time = document.createElement("p");
    time.classList.add("time");
    time.textContent = timeStart + " - " + timeEnd;
    taskUI.appendChild(time);

    let status = document.createElement("p");
    status.classList.add("fixedStatus");
    status.textContent = statusText;
    taskUI.appendChild(status);

    document.getElementById("scrollable-task-view").appendChild(taskUI);
}

function clearTaskList() {
    let taskView = document.getElementById("scrollable-task-view");
    taskView.innerHTML = ""; // gets rid of children
}
