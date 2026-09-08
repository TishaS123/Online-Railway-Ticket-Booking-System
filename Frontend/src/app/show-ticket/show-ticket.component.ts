import { Component, ElementRef, ViewChild } from '@angular/core';
import { ReservationService } from '../services/reservation.service';
import html2canvas from 'html2canvas';
import jsPDF from 'jspdf';
import { Router } from '@angular/router';
import { Ticket } from '../ticket.model';

@Component({
  selector: 'app-show-ticket',
  templateUrl: './show-ticket.component.html',
  styleUrls: ['./show-ticket.component.css']
})
export class ShowTicketComponent {
  ticket !: Ticket;

  
  @ViewChild('ticketRef', { static: false }) ticketRef!: ElementRef;


  constructor(private reservationService: ReservationService, private router: Router){
    this.ticket = this.reservationService.getTicket();
  }

  ngOnInit(): void {
    if (!this.ticket || !this.ticket.ticketNo) {
      this.router.navigate(['/user-home']);
      return;
    }

    this.reservationService.getTicketById(this.ticket.ticketNo).subscribe({
      next: (freshTicket) => {
        this.ticket = freshTicket;
        this.reservationService.setTicket(freshTicket);
      },
      error: () => {
        // Keep the in-memory ticket as a fallback.
      }
    });
  }

  
downloadPDF(): void {
    const ticketElement = this.ticketRef.nativeElement;
    html2canvas(ticketElement).then(canvas => {
      const imgData = canvas.toDataURL('image/png');
      const pdf = new jsPDF();
      const imgProps = pdf.getImageProperties(imgData);
      const pdfWidth = pdf.internal.pageSize.getWidth();
      const pdfHeight = (imgProps.height * pdfWidth) / imgProps.width;
      pdf.addImage(imgData, 'PNG', 0, 0, pdfWidth, pdfHeight);
      pdf.save('Train_Ticket.pdf');
    });
  }

  
printTicket(): void {
    const printContents = this.ticketRef.nativeElement.innerHTML;
    const popupWin = window.open('', '_blank', 'width=800,height=600');
    if (popupWin) {
      popupWin.document.open();
      popupWin.document.write(`
        <html>
          <head>
            <title>Print Ticket</title>
          </head>
          <body onload="window.print();window.close()">
            ${printContents}
          </body>
        </html>
      `);
      popupWin.document.close();
    }
  }

  goBack(){
    this.router.navigate(['/user-home']);
  }



}
