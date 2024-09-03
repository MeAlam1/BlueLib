const resizer = document.querySelector('.resizer');
const topPanel = document.querySelector('.top-panel');
const bottomPanel = document.querySelector('.bottom-panel');

let isResizing = false;

resizer.addEventListener('mousedown', function(e) {
    isResizing = true;
    document.body.style.cursor = 'row-resize';
    document.body.style.userSelect = 'none';
});

document.addEventListener('mousemove', function(e) {
    if (!isResizing) return;

    const offset = e.clientY - topPanel.getBoundingClientRect().top;
    topPanel.style.flexGrow = 0;
    bottomPanel.style.flexGrow = 0;
    topPanel.style.height = `${offset}px`;
    bottomPanel.style.height = `calc(100% - ${offset}px - 3px)`;
});

document.addEventListener('mouseup', function() {
    isResizing = false;
    document.body.style.cursor = '';
    document.body.style.userSelect = '';
});