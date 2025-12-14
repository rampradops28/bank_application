 
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
    box.style.display = "block";
 
    if (Array.isArray(data)) {
        if (data.length === 0) {
            box.innerHTML = "<p>No transactions found.</p>";
            return;
        }

        let html = `
            <h4>Transaction History</h4>
            <table>
                <tr> 
                    <th>Type</th>
                    <th>Amount</th>
                    <th>Before</th>
                    <th>After</th>
                    <th>Date</th>
                </tr>
        `;

        data.forEach(tx => {
            html += `
                <tr> 
                    <td>${tx.paymentType}</td>
                    <td>${tx.amount}</td>
                    <td>${tx.balanceBefore}</td>
                    <td>${tx.balanceAfter}</td>
                    <td>${tx.transactionDate}</td>
                </tr>
            `;
        });

        html += "</table>";
        box.innerHTML = html;
        return;
    }
 
    if (!data || typeof data !== "object") {
        box.innerHTML = `<h4>Error</h4><p>${data}</p>`;
        return;
    }
 
    let html = `<h4>Response</h4>`;
    for (const key in data) {
        html += `<div class="response-field"><span>${key}:</span> ${data[key]}</div>`;
    }

    box.innerHTML = html;
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
        date: pt_date.value
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

    const res = await fetch(
        `http://localhost:8080/api/v1/transactions/${posting}`
    );

    showResponse("retrieveTransactionOutput", await res.json());
}
 

async function loadTransactionHistory() {
    const accountNumber = th_accountNumber.value;

    if (!accountNumber) {
        alert("Please enter account number");
        return;
    }

    const res = await fetch(
        `http://localhost:8080/api/v1/accounts/${accountNumber}/transactions`
    );

    showResponse("transactionHistoryOutput", await res.json());
}
