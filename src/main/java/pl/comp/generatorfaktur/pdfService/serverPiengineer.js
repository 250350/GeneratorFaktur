const express = require("express");
const puppeteer = require("puppeteer");

const app = express();

app.use(express.json({ limit: "10mb" }));
app.use(express.text({ type: "*/*" }));

app.post("/pdf", async (req, res) => {
    try {
        const { html, logoBase64 } = req.body;

        console.log("HTML LENGTH:", html?.length);
        console.log("LOGO EXISTS:", !!logoBase64);

        const browser = await puppeteer.launch({
            headless: "new"
        });

        const page = await browser.newPage();

        await page.setContent(html, {
            waitUntil: "networkidle0"
        });

        await page.emulateMediaType("print");

        const pdf = await page.pdf({
            format: "A4",
            printBackground: true,

            displayHeaderFooter: true,

            margin: {
                top: "30px",
                bottom: "40px",
                left: "0mm",
                right: "0mm"
            },

            headerTemplate: `
                <div style="
                    width: 100%;
                    padding: 0 20mm;
                    box-sizing: border-box;
                    font-size: 10px;
                ">
                    <div style="margin-top: 20px;">
                        <img src="data:image/png;base64,${logoBase64 || ""}"
                             style="height: 30px;" />
                    </div>

                    <div style="
                        margin-top: 10px;
                        border-top: 1px solid black;
                        width: 100%;
                    "></div>
                </div>
            `,

            footerTemplate: `
              <div style="
                width: 100%;
                padding: 0 20mm;
                box-sizing: border-box;
                font-size: 9px;
                position: relative;
              ">
                <div style="
                  position: absolute;
                  bottom: 15px;
                  left: 20mm;
                  right: 20mm;
                  display: flex;
                  justify-content: space-between;
                ">

                  <!-- KOLUMNA 1 -->
                  <div style="width: 23%; text-align: left;">
                    <div>PiEngineer</div>
                    <div>Przemysław Piechota</div>
                    <div>Ul. Niciarniana 45</div>
                    <div>92-320 Łódź</div>
                  </div>

                  <!-- KOLUMNA 2 -->
                  <div style="width: 23%; text-align: left;">
                    <div>Regon: 362358675</div>
                    <div>nipBuyer: 726-108-80-84</div>
                    <div>VAT-UE PL 726-108-80-84</div>
                  </div>

                  <!-- KOLUMNA 3 -->
                  <div style="width: 23%; text-align: left;">
                    <div>Telefon: +48 66 4848488</div>
                    <div>Telefax:</div>
                    <div>Internet: przemyslaw.piechota@wp.pl</div>
                  </div>

                  <!-- KOLUMNA 4 -->
                  <div style="width: 23%; text-align: left;">
                    <div>mBank S.A.</div>
                    <div>SWIFT (BIC): BREXPLPWMBK</div>
                    <div>IBAN (PLN): PL25 1140 2004 0000 3202 7584 2595</div>
                    <div>IBAN (EUR):</div>
                  </div>

                </div>
              </div>
            `
        });

        console.log("PDF GENERATED:", pdf?.length);

        await browser.close();

        res.setHeader("Content-Type", "application/pdf");
        res.send(pdf);

    } catch (e) {
        console.error("PDF ERROR:", e);
        res.status(500).send("PDF ERROR");
    }
});

app.listen(3001, () => {
    console.log("PDF service running on port 3001");
});