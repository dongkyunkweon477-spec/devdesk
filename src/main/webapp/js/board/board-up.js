// Handle file selection for automatic upload
document.getElementById('imageFile').addEventListener('change', function (e) {
    e.preventDefault();

    const fileInput = this;
    const formData = new FormData();
    formData.append('file', fileInput.files[0]);

    if (fileInput.files.length === 0) {
        return;
    }

    console.log('File selected, starting upload...');

    fetch('supa-upload', {
        method: 'POST',
        body: formData
    })
        .then(response => response.text())
        .then(data => {
            console.log('Server response:', data);

            let imageUrl = null;

            let urlMatch = data.match(/(https:\/\/[a-zA-Z0-9-]+\.supabase\.co\/storage\/v1\/object\/public\/upload\/file\/[^\s]+)/);
            if (urlMatch) {
                imageUrl = urlMatch[1];
            }

            if (!imageUrl) {
                urlMatch = data.match(/(https:\/\/[^\s]+)/);
                if (urlMatch) {
                    imageUrl = urlMatch[1];
                }
            }

            if (imageUrl) {
                console.log('Extracted URL:', imageUrl);
                const textarea = document.querySelector("textarea[name='content']");
                textarea.value += '\n\n' + imageUrl;
                fileInput.value = '';
                console.log('Image uploaded and URL inserted:', imageUrl);
                alert('Image uploaded successfully!');
            } else {
                console.error('URL extraction failed. Response:', data);
                alert('Failed to extract image URL from response. Check console for details.');
            }
        })
        .catch(error => {
            console.error('Upload error:', error);
            alert('Image upload failed. Please try again.');
        });
});