const resizer = document.querySelector('.resizer');
const topPanel = document.querySelector('.top-panel');
const bottomPanel = document.querySelector('.bottom-panel');
const leftPanel = document.querySelector('.left-panel');

let startY;
let startHeight;

resizer.addEventListener('mousedown', (e) => {
    startY = e.clientY;
    startHeight = topPanel.offsetHeight;
    document.addEventListener('mousemove', handleMouseMove);
    document.addEventListener('mouseup', handleMouseUp);
});

function handleMouseMove(e) {
    const height = startHeight + (e.clientY - startY);
    topPanel.style.height = `${height}px`;
    bottomPanel.style.height = `${leftPanel.offsetHeight - height - resizer.offsetHeight}px`;
}

function handleMouseUp() {
    document.removeEventListener('mousemove', handleMouseMove);
    document.removeEventListener('mouseup', handleMouseUp);
}