 
document.querySelectorAll(".tab-btn").forEach(btn => {
    btn.addEventListener("click", () => {
        document.querySelectorAll(".tab-btn").forEach(b => b.classList.remove("active"));
        document.querySelectorAll(".tab-content").forEach(tc => tc.classList.remove("active"));
        btn.classList.add("active");
        document.getElementById(btn.dataset.tab).classList.add("active");
    });
});
 
function showResponse(domId, data) {
    const box = document.getElementById(domId);

    if (!data || typeof data !== "object") {
        box.style.display = "block";
        box.innerHTML = `<h4>Error</h4><p>${data}</p>`;
        return;
    }

    let html = `<h4>Response</h4>`;
    for (const key in data) {
        html += `<div class="response-field"><span>${key}:</span> ${data[key]}</div>`;
    }

    box.innerHTML = html;
    box.style.display = "block";
} 

async function createAccount() {
    const body = {
        productCode: ca_productCode.value,
        productType: ca_productType.value,
        name: ca_name.value,
        age: Number(ca_age.value),
        accountNumber: ca_accountNumber.value
    };

    const res = await fetch("http://localhost:8080/api/v1/accounts", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
    });

    showResponse("createAccountOutput", await res.json());
}

async function retrieveAccount() {
    const acc = ra_accountNumber.value;
    const type = ra_productType.value;

    const res = await fetch(
        `http://localhost:8080/api/v1/accounts?accountNumber=${acc}&productType=${type}`
    );

    showResponse("retrieveAccountOutput", await res.json());
}

async function postTransaction() {
    const body = {
        accountNumber: pt_accountNumber.value,
        paymentType: pt_paymentType.value,
        amount: Number(pt_amount.value),
        postingDate: pt_date.value
    };

    const res = await fetch("http://localhost:8080/api/v1/transactions", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
    });

    showResponse("postTransactionOutput", await res.json());
}

async function retrieveTransaction() {
    const posting = rt_postingNumber.value;
    const res = await fetch(`http://localhost:8080/api/v1/transactions/${posting}`);

    showResponse("retrieveTransactionOutput", await res.json());
}
