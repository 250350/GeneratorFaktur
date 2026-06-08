console.log("script.js loaded");

function printInvoice(){
    window.print();
}

window.generateLink = function () {
    const link = `${window.location.origin}/f/${invoiceId}`;

    console.log("clicked");

    navigator.clipboard.writeText(link)
        .then(() => alert("Copied: " + link));
}
// async function generateLink() {
//
//     const res = await fetch(`/stripe/invoice/share`, {
//         method: "POST",
//         headers: {
//             "Content-Type": "application/x-www-form-urlencoded"
//         },
//         body: new URLSearchParams({
//             invoiceId: invoiceId
//         })
//     });
//
//     const data = await res.text();
//
//     // backend zwraca redirect URL
//     window.location.href = data;
// }

