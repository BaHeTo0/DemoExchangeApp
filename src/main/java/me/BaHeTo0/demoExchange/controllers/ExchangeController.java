package me.BaHeTo0.demoExchange.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.BaHeTo0.demoExchange.config.CurrentUser;
import me.BaHeTo0.demoExchange.models.entities.ExchangeTransactionEntity;
import me.BaHeTo0.demoExchange.models.entities.User;
import me.BaHeTo0.demoExchange.models.request.ConvertRequest;
import me.BaHeTo0.demoExchange.models.responses.ErrorInfo;
import me.BaHeTo0.demoExchange.services.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Tag(name = "exchange-controller", description = "Handles currency exchange operations like convert, rate, and history")
@RestController
@RequestMapping("/exchange-api")
public class ExchangeController {

    @Autowired
    private ExchangeService exchangeService;


    @GetMapping("/currencies")
    public ResponseEntity<List<String>> getCurrencies(
            @Parameter(description = "User ID header", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
            @RequestHeader("X-User-Id") String userIdHeader
    ) {
        return ResponseEntity.ok(exchangeService.getCurrencies());
    }

    @GetMapping("/rate")
    public ResponseEntity<BigDecimal> getExchangeRate(
            @Parameter(description = "User ID header", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
            @RequestHeader("X-User-Id") String userIdHeader,
            @RequestParam String from,
            @RequestParam String to
    ) {
        return ResponseEntity.ok(exchangeService.getRate(from, to));
    }

    @Operation(
            summary = "Convert currency",
            description = "Converts a specified amount from one currency to another for the authenticated user",
            parameters = {
                    @Parameter(name = "X-User-Id", description = "User ID header", required = true, in = ParameterIn.HEADER)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Conversion successful"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data",content = @Content(schema = @Schema(implementation = ErrorInfo.class))),
                    @ApiResponse(responseCode = "401", description = "Missing or invalid authentication header", content = @Content(schema = @Schema(implementation = ErrorInfo.class)))
            }
    )
    @PostMapping("/convert")
    public ResponseEntity<ExchangeTransactionEntity> convert(
            @Parameter(hidden = true) @CurrentUser User user,
            @Parameter(description = "User ID header", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
            @RequestHeader("X-User-Id") String userIdHeader,
            @RequestBody ConvertRequest request
    ) {
        return ResponseEntity.ok(exchangeService.convert(user, request.getAmount(), request.getFromCurrency(), request.getToCurrency()));
    }

    @GetMapping("/history")
    public ResponseEntity<List<ExchangeTransactionEntity>> history(
            @Parameter(description = "User ID header", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
            @RequestHeader("X-User-Id") String userIdHeader,
            @RequestParam(required = false) UUID id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate date,
            @RequestParam Integer page,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) UUID userId
    ) {
        return ResponseEntity.ok(exchangeService.transactionHistory(page, pageSize, date, id, userId));
    }
}
