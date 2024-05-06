document.addEventListener("DOMContentLoaded", function(event){
    const btnExport = document.getElementById("btn-export");
    const btnImport = document.getElementById("btn-import");
    const fileInput = document.getElementById('file-input');
    
    btnImport.addEventListener("click", function (){
        fileInput.click();
    });
    
    fileInput.addEventListener("change", function () {
        // Check if any file is selected
        if (fileInput.files.length > 0) {
            const files = fileInput.files[0];
            // Check file type
            if (!files.name.endsWith('.xlsx')) {
                alert('Vui lòng chọn một tệp Excel (.xlsx).');
                return;
            }
            // Create a FormData object and append the file to it
            const file = new FormData();
            file.append('file', files);

            // Send import request when a file is selected
            fetch('/thietbi/import-excel', {
                method: 'POST',
                body: file // Use FormData containing file data
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                // If import is successful, do something if needed
                console.log('Import successful');
                window.location.href='/thietbi';
            })
            .catch(error => {
                console.error('There was an error with the fetch operation:', error);
            });
        } else {
            console.log("No file selected");
        }
    });
    
    btnExport.addEventListener("click", function (){
        // Send Excel export request when the button is clicked
        fetch('/thietbi/export-excel', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            // Return excel file from response
            return response.blob();
        })
        .then(blob => {
            // Create a URL from blob data
            const url = window.URL.createObjectURL(new Blob([blob]));
            // Create an 'a' element to download the excel file
            const a = document.createElement('a');
            a.href = url;
            a.download = 'thietbi.xlsx'; // File name when downloading
            // Append 'a' element to the DOM and trigger click event to start downloading
            document.body.appendChild(a);
            a.click();
            // After downloading, revoke the 'a' element
            window.URL.revokeObjectURL(url);
        })
        .catch(error => {
            console.error('There was an error with the fetch operation:', error);
        });
    });
});

// Pagination
const paginationNav = document.querySelector(".pagination-container");
const curPage = Number.parseInt(paginationNav.dataset.pageNo);
const totalPages = Number.parseInt(paginationNav.dataset.totalPages);
const query = paginationNav.dataset.query;
const urlArgs = { query };
const pagination = new Pagination(
  "/thietbi/search",
  urlArgs,
  curPage,
  totalPages
);
