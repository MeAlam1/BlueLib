const resizerHorizontal = document.querySelector('.resizer-horizontal');
const resizerVertical = document.querySelector('.resizer-vertical');
const topPanel = document.querySelector('.top-panel');
const bottomPanel = document.querySelector('.bottom-panel');
const leftPanel = document.querySelector('.left-panel');
const mainContent = document.querySelector('.main-content');

let isResizingHorizontal = false;
let isResizingVertical = false;

resizerHorizontal.addEventListener('mousedown', function(e) {
    isResizingHorizontal = true;
    document.body.style.cursor = 'row-resize';
    document.body.style.userSelect = 'none';
});

resizerVertical.addEventListener('mousedown', function(e) {
    isResizingVertical = true;
    document.body.style.cursor = 'col-resize';
    document.body.style.userSelect = 'none';
});

document.addEventListener('mousemove', function(e) {
    if (isResizingHorizontal) {
        const offset = e.clientY - topPanel.getBoundingClientRect().top;
        topPanel.style.flexGrow = 0;
        bottomPanel.style.flexGrow = 0;
        topPanel.style.height = `${offset}px`;
        bottomPanel.style.height = `calc(100% - ${offset}px - 2px)`;
    }

    if (isResizingVertical) {
        const offset = e.clientX - leftPanel.getBoundingClientRect().left;
        leftPanel.style.width = `${offset}px`;
        mainContent.style.flexGrow = 1;
    }
});

document.addEventListener('mouseup', function() {
    isResizingHorizontal = false;
    isResizingVertical = false;
    document.body.style.cursor = '';
    document.body.style.userSelect = '';
});