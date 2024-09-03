import { leftPanelContent } from './Content.js';
import { setupResizing } from './ResizingLogic.js';
import { setupEventListeners } from './EventListeners.js';

document.addEventListener('DOMContentLoaded', function() {
    const leftPanelContainer = document.getElementById('left-panel-container');

    if (leftPanelContainer) {
        leftPanelContainer.innerHTML = leftPanelContent;
    }

    setupResizing();
    setupEventListeners();
});