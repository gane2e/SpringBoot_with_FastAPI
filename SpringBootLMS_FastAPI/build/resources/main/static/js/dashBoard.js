function printCertificate() {

    var userName = document.getElementById('userName').value;
    var certificationNumber = document.getElementById('certificationNumber').value;
    var courseTitle = document.getElementById('courseTitle').value;
    var completionDateTime = document.getElementById('completionDateTime').value;
    var birthDate = document.getElementById('birthDate').value;
    var date = new Date(completionDateTime);
    var date2 = new Date(birthDate);
    document.title = userName + "_수료증_" + certificationNumber;

    var formattedDate = date.getFullYear() + '-' +
        (date.getMonth() + 1).toString().padStart(2, '0') + '-' +
        date.getDate().toString().padStart(2, '0');

    var formattedDate2 = date2.getFullYear() + '-' +
        (date2.getMonth() + 1).toString().padStart(2, '0') + '-' +
        date2.getDate().toString().padStart(2, '0');


    var printWindow = window.open('', '_blank', 'width=800,height=600');
    printWindow.document.write(`
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
      xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>수료증</title>
  <style>
    @import url('https://fonts.googleapis.com/css2?family=Noto+Serif+KR:wght@200..900&display=swap');
    @page {
      size: A4;
      margin: 0;
    }
    @media print {
      img {
        display: inline !important;
        visibility: visible !important;
        max-width: 100% !important; /* 이미지 크기를 조정 */
      }
      .page {
        margin: 0;
        border: initial;
        border-radius: initial;
        box-shadow: initial;
        page-break-after: always;
      }
    }
    body {
      font-family: "Noto Serif KR", serif;
      font-optical-sizing: auto;
    }
    * {
      box-sizing: border-box;
      -moz-box-sizing: border-box;
      -webkit-print-color-adjust: exact !important;   /* Chrome, Safari, Edge */
      color-adjust: exact !important;                 /*Firefox*/
    }
    body{
      margin: 0;
      padding: 0;
    }
    .page {
      display: block;
      width: 785px;
      height: 1118px;
      position:relative;
      margin: 0 auto;
    }
    .page > img {
      width: 100%;
    }
    .page_cont{
      position: absolute;
      top: 0;
      left: 0;
      width: 793px;
      height: 1122px;
      padding: 1.5cm 1.5cm 2cm 1.5cm;
    }
    .completionNum {
      line-height: 35px;
      font-weight: 800;
      font-size: 24px;
      padding: 50px;
      float: right;
    }
    .title {
      margin-top: 167px;
      margin-left: 243px;
      font-weight: 800;
      font-size: 40px;
      line-height: 63px;
      color: #000;
    }
    .content {

    }
    table {
      margin-top: 100px;
      margin-left: 90px;
      margin-right: 60px;
    }
    th, td {
      text-align: left;
      font-weight: 700;
      font-size: 24px;
      vertical-align: top; 
      height: 30px;
    }

    td{
      padding-left: 15px;
    }

    .txt {
      margin-top: 95px;
      margin-left: 15px;
      text-align: center;
      line-height: 35px;
      font-weight: 900;
      font-size: 22px;
    }
    .director {
      position: absolute;
      top: 880px;
      left: 280px;
      text-align: center;
      line-height: 35px;
      font-weight: 900;
      font-size: 26px;
      z-index: 1;
    }
    .sample {
      position: absolute;
      width: 80px;
      top: 855px; 
      left: 470px;
      z-index: 0; 
    }
  </style>
</head>
<body>


<img src="/img/certificate_image.png" 
    class="page">
<div class="page_cont">
     <div class="completionNum">제 ${certificationNumber}호</div>
  <div class="title">교육 수료증</div>
  <div class="content">
    <table>
      <tr>
        <th>성&nbsp;&nbsp;&nbsp;&nbsp;
          &nbsp;&nbsp;&nbsp;&nbsp;
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;명&nbsp;&nbsp;:
         </th>
        <td> ${userName}</td>
      </tr>
      <tr>
        <th>생&nbsp;&nbsp;&nbsp;&nbsp;년&nbsp;&nbsp;&nbsp;&nbsp;월&nbsp;&nbsp;&nbsp;일&nbsp;&nbsp;:</th>
        <td> ${formattedDate2}</td>
      </tr>
      <tr>
        <th>교&nbsp;&nbsp;육&nbsp;&nbsp;과&nbsp;&nbsp;정&nbsp;&nbsp;명&nbsp;&nbsp;:</th>
        <td> ${courseTitle}</td>
      </tr>
      <tr>
        <th>수&nbsp;&nbsp;&nbsp;&nbsp;료&nbsp;&nbsp;&nbsp;&nbsp;일&nbsp;&nbsp;&nbsp;자&nbsp;&nbsp;:</th>
        <td> ${formattedDate}</td>
      </tr>
    </table>
    <div class="txt">
      귀하는 위의 과정에 참여하여 교육과정을 수료하였음을 <br>확인합니다.
    </div>
    <div class="director">온라인학습 센터장</div>
    <img src="/img/샘플직인.png" class="sample" alt="직인">
  </div>
</img>
</div>
 
</body>
</html>
    `);
    // 새 창에서 이미지를 로드한 후 인쇄
    var img = printWindow.document.querySelector('.sample');
    img.onload = function() {
        // 이미지가 로드된 후 인쇄
        printWindow.document.close();
        printWindow.print();
    };
    // 이미지가 이미 로드된 경우 즉시 인쇄
    if (img.complete) {
        printWindow.document.close();
        printWindow.print();
    }

    printWindow.onafterprint = function() {
        printWindow.close();
    };

}