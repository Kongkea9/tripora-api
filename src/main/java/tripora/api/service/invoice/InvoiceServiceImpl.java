package tripora.api.service.invoice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tripora.api.Util.security.SecurityUtil;
import tripora.api.domain.Booking;
import tripora.api.domain.Invoice;
import tripora.api.dto.InvoiceResponse;
import tripora.api.exception.BadRequestException;
import tripora.api.exception.ConflictException;
import tripora.api.exception.ResourceNotFoundException;
import tripora.api.mapper.InvoiceMapper;
import tripora.api.repository.BookingRepository;
import tripora.api.repository.InvoiceRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final BookingRepository bookingRepository;
    private final InvoiceMapper invoiceMapper;


    @Override
    public List<InvoiceResponse> myInvoices() {

        Integer userId = SecurityUtil.getCurrentUserId();

        return invoiceRepository.findAllByBookingUserId(userId)
                .stream()
                .map(invoiceMapper::mapToInvoiceResponse)
                .toList();
    }

    @Override
    public InvoiceResponse getMine(Integer invoiceId) {

        Integer userId = SecurityUtil.getCurrentUserId();

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        if (!invoice.getBooking().getUser().getId().equals(userId)) {
            throw new ConflictException("Not your invoice");
        }

        return invoiceMapper.mapToInvoiceResponse(invoice);
    }

    @Override
    public List<InvoiceResponse> getAll(String status) {

        List<Invoice> invoices = (status == null || status.isBlank())
                ? invoiceRepository.findAll()
                : invoiceRepository.findAllByStatus(status);

        return invoices.stream()
                .map(invoiceMapper::mapToInvoiceResponse)
                .toList();
    }

    @Override
    public InvoiceResponse getById(Integer id) {

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        return invoiceMapper.mapToInvoiceResponse(invoice);
    }


}