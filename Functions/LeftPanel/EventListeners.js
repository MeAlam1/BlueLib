import { handlePackageClick } from './PackageClick.js';
import { handleClassClick } from './ClassClick.js';

export function setupEventListeners() {
    document.querySelectorAll('.package-item').forEach(function(item) {
        item.addEventListener('click', function() {
            const id = item.getAttribute('data-id');
            handlePackageClick(id);
        });
    });

    document.querySelectorAll('.class-item').forEach(function(item) {
        item.addEventListener('click', function() {
            const id = item.getAttribute('data-id');
            handleClassClick(id);
        });
    });
}
