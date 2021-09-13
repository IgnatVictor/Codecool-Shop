/* When the user clicks on the button,
toggle between hiding and showing the dropdown content */


function CategoriesDropdown() {
    document.getElementById("CategoriesDropdown").classList.toggle("show");
    document.getElementById("SuppliersDropdown").classList.remove("show");

}

function SuppliersDropdown() {
    document.getElementById("SuppliersDropdown").classList.toggle("show");
    document.getElementById("CategoriesDropdown").classList.remove("show");
}

window.onclick = function (event) {
    if (!event.target.matches('.dropbtn')) {

        var dropdowns = document.getElementsByClassName("dropdown-content");
        var i;
        for (i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    }
};