const puppeteer = require('puppeteer');
const path = require('path');

(async () => {
    const filePath = path.resolve(__dirname, '../templates/report.html');
    const fileUrl = `file://${filePath}`;

    const browser = await puppeteer.launch({
        headless: true,
        args: ['--no-sandbox']
    });

    const page = await browser.newPage();

    await page.goto(fileUrl, {waitUntil: 'networkidle0'});

    // انتظر لغاية ما الرسوم ترندر
    await page.waitForSelector('#pieChart');
    // await page.waitForTimeout(2000); // مهم جداً عشان الـ Chart.js يرندر كويس

    const pdfBuffer = await page.pdf({format: 'A4', printBackground: true});

    await browser.close();

    process.stdout.write(pdfBuffer);
})();
