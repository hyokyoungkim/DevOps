document.addEventListener('DOMContentLoaded', function () {


            const leftView = document.getElementById('leftView');
            const bothView = document.getElementById('bothView');
            const rightView = document.getElementById('rightView');
            const allArea = document.getElementById('allArea');

            const contentInput = document.getElementById('contentInput');
            const italicIcon = document.getElementById('italicIcon');
            const slashIcon = document.getElementById('slashIcon');
            const colonIcon = document.getElementById('colonIcon');
            const titleIcon = document.getElementById('titleIcon');
            const titleGroupIcon = document.getElementById('titleGroupIcon');
            const previewArea = document.getElementById('previewArea');


            if (!contentInput || !italicIcon || !slashIcon || !colonIcon || !titleIcon || !titleGroupIcon || !previewArea) {
                console.error('One or more elements not found');
                return;
            }

            // Event listeners for style buttons
            italicIcon.addEventListener('click', () => applyStyle('italic'));
            slashIcon.addEventListener('click', () => applyStyle('code'));
            colonIcon.addEventListener('click', () => applyStyle('quote'));

            // Show or hide title group when titleIcon is clicked
    titleIcon.addEventListener('click', () => {
        console.log('ㄴㅇㄹㄴㄻㅇㄻㄴ');
                titleGroupIcon.style.display = titleGroupIcon.style.display === 'none' ? 'block' : 'none';
            });

            // Adding event listeners for title buttons
            titleGroupIcon.querySelectorAll('button').forEach(button => {
                button.addEventListener('click', (event) => {
                    const titleId = event.currentTarget.getAttribute('data-name'); // Use data-name attribute
                    const level = parseInt(titleId.replace('title', '')); // Extract the title level
                    applyStyle(`title${level}`);
                    titleGroupIcon.style.display = 'none'; // Hide the title group after selection
                });
            });

            // Function to apply style based on button clicked
            function applyStyle(style) {
                const start = contentInput.selectionStart;
                const end = contentInput.selectionEnd;
                const content = contentInput.value;
                const selectedText = content.substring(start, end);

                let newContent;
                if (style === 'italic') {
                    newContent = `${content.substring(0, start)}*${selectedText}*${content.substring(end)}`;
                } else if (style === 'code') {
                    newContent = `${content.substring(0, start)}\`${selectedText}\`${content.substring(end)}`;
                } else if (style === 'quote') {
                    newContent = `${content.substring(0, start)}> ${selectedText}${content.substring(end)}`;
                } else if (style.startsWith('title')) {
                    const level = parseInt(style.replace('title', ''));
                    newContent = `${content.substring(0, start)}${'#'.repeat(level)} ${selectedText}${content.substring(end)}`;
                }

                contentInput.value = newContent;
                contentInput.setSelectionRange(start, start + newContent.length - content.length + selectedText.length);
                updatePreview();
            }

            // Function to update preview area
            function updatePreview() {
                const markdownContent = contentInput.value;

                // Markdown to HTML conversion
                const htmlContent = markdownContent
                    .replace(/\*\*([^*]+)\*\*/g, '<strong>$1</strong>') // Bold
                    .replace(/\*([^*]+)\*/g, '<i>$1</i>')              // Italic
                    .replace(/`([^`]+)`/g, '<code>$1</code>')          // Code
                    .replace(/> (.+)/g, '<blockquote>$1</blockquote>') // Blockquote
                    .replace(/^(#{1}) (.+)$/gm, '<h1>$2</h1>')         // Heading 1
                    .replace(/^(#{2}) (.+)$/gm, '<h2>$2</h2>')         // Heading 2
                    .replace(/^(#{3}) (.+)$/gm, '<h3>$2</h3>')         // Heading 3
                    .replace(/^(#{4}) (.+)$/gm, '<h4>$2</h4>')         // Heading 4
                    .replace(/^(#{5}) (.+)$/gm, '<h5>$2</h5>')         // Heading 5
                    .replace(/^(#{6}) (.+)$/gm, '<h6>$2</h6>')         // Heading 6
                    .replace(/\n/g, '<br/>');                          // Line break

                previewArea.innerHTML = htmlContent;
            }

            // Real-time preview update
            contentInput.addEventListener('input', updatePreview);
        });

