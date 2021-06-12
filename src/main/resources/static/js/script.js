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

// first request to server to create order
const paymentStart = () =>{
var amount = $("#payment_field").val();
console.log(amount);
if(amount=="" || amount == null  ){
 swal("Failed !", "Amount is required!", "error");
return;
}

 // code...
 // using ajax to send request to server to create order
 $.ajax({
        url:"/user/create_order",
        data:JSON.stringify({amount:amount,info:"order_request"}),
        contentType:"application/json",
        type:"POST",
        dataType:"json",
        success:function(response){
        console.log(response);
        if(response.status=='created'){
            // open payment form
            let options = {
                "key": "rzp_test_7Q4vwoisbJSk77",
                "amount": response.amount,
                "currency": "INR",
                "name": "Smart Contact Manager - SCM Donation",
                "description": "Donation",
                "image": "https://github.com/daadestroyer/daadestroyer.github.io/blob/main/img/img-mobile.PNG?raw=true",
                "order_id": response.id, //This is a sample Order ID. Pass the `id` obtained in the response of Step
                 "handler": function (response){
                 console.log(response.razorpay_payment_id);
                 console.log(response.razorpay_order_id);
                 console.log(response.razorpay_signature)
                 swal("Congrats !", "Payment Successfull!", "success");
                  },
                  "prefill": {
                          "name": "Shubham Nigam",
                          "email": "nigamshubham1998@gmail.com",
                          "contact": "8840585165"
                      },
                      "notes": {
                          "address": "Smart Contact Manager - SCM Office"
                      },
                      "theme": {
                          "color": "#3399cc"
                      }
            };

            let rzp = new Razorpay(options);
            rzp.on('payment.failed', function (response){
                    console.log(response.error.code);
                    console.log(response.error.description);
                    console.log(response.error.source);
                    console.log(response.error.step);
                    console.log(response.error.reason);
                    console.log(response.error.metadata.order_id);
                    console.log(response.error.metadata.payment_id);
                    swal("Failed !", "OOPS... Payment Failed!", "error");
            });
            rzp.open();

        }
        },
        error:function(error){
         // invoked when error come
         console.log(error);
         console.log("something went wrong");
        }
 });
};