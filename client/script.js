 async function createAccount() {
        const body = {
            productCode: document.getElementById("ca_productCode").value,
            productType: document.getElementById("ca_productType").value,
            name: document.getElementById("ca_name").value,
            age: Number(document.getElementById("ca_age").value),
            accountNumber: document.getElementById("ca_accountNumber").value,
        };

        const res = await fetch("http://localhost:8080/api/v1/accounts", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(body)
        });

        document.getElementById("createAccountOutput").textContent =
            JSON.stringify(await res.json(), null, 2);
    }

    async function retrieveAccount() {
        const acc = document.getElementById("ra_accountNumber").value;
        const type = document.getElementById("ra_productType").value;

        const res = await fetch(`http://localhost:8080/api/v1/accounts?accountNumber=${acc}&productType=${type}`);

        document.getElementById("retrieveAccountOutput").textContent =
            JSON.stringify(await res.json(), null, 2);
    }

    async function postTransaction() {
        const body = {
            accountNumber: document.getElementById("pt_accountNumber").value,
            paymentType: document.getElementById("pt_paymentType").value,
            amount: Number(document.getElementById("pt_amount").value),
            postingDate: document.getElementById("pt_date").value,
        };

        const res = await fetch("http://localhost:8080/api/v1/transactions", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(body)
        });

        document.getElementById("postTransactionOutput").textContent =
            JSON.stringify(await res.json(), null, 2);
    }

    async function retrieveTransaction() {
        const posting = document.getElementById("rt_postingNumber").value;

        const res = await fetch(`http://localhost:8080/api/v1/transactions/${posting}`);

        document.getElementById("retrieveTransactionOutput").textContent =
            JSON.stringify(await res.json(), null, 2);
    }