const toggleSidebar = () => {
    if ($(".sidebar").is(":visible")) {
        //true
        //band karna hai
        $(".sidebar").css("display", "none");
        $(".content").css("margin-left", "0%");
    } else {
        //false
        //show karna hai
        $(".sidebar").css("display", "block");
        $(".content").css("margin-left", "20%");
    }
};

const search =    () => {
    let query = $("#search-input").val();

    if (query == "") {
        $(".search-result").hide();
    } else {
        // search
        console.log(query);

        // sending request to server
        let url = `http://localhost:8080/search/${query}`;

        fetch(url)
            .then((response) => {
                return response.json();
            })
            .then((data) => {
                // data...
                
                let text = `<div class='list-group'>`;
                data.forEach((contact) => {
                    text+=`<a href='/user/profile/${contact.cId}' class='list-group-item list-group-action  style='color:red;'  '>${contact.name}</a>`
                });
                text += `</div>`;

                $(".search-result").html(text);
                $(".search-result").show();
            });
    }
};