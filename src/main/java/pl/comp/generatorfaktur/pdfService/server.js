const express = require("express");
const puppeteer = require("puppeteer");

const app = express();

app.use(express.json({ limit: "10mb" }));
app.use(express.text({ type: "*/*" }));

app.post("/pdf", async (req, res) => {
    try {
        const html = req.body.html;

        console.log("HTML LENGTH:", html?.length);
        console.log("HTML START:", html?.substring(0, 200));

        const browser = await puppeteer.launch({
            headless: "new"
        });

        const page = await browser.newPage();

        await page.setContent(html, {
            waitUntil: "networkidle0"
        });

        await page.emulateMediaType("screen");

        const pdf = await page.pdf({
            format: "A4",
            printBackground: true
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