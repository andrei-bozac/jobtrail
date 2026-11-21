<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="titluPagina" value="Tablou" scope="request"/>
<c:set var="meniuActiv" value="tablou" scope="request"/>
<%@ include file="/WEB-INF/views/fragmente/antet.jspf" %>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h1 class="h3 mb-0">Tabloul aplicărilor</h1>
    <a href="${pageContext.request.contextPath}/aplicari/nou" class="btn btn-primary">
        <i class="bi bi-plus-lg"></i> Aplicare nouă
    </a>
</div>

<!-- Statistici -->
<div class="row g-3 mb-4">
    <div class="col-6 col-md-3">
        <div class="card shadow-sm h-100"><div class="card-body">
            <div class="text-muted small">Total aplicări</div>
            <div class="fs-3 fw-semibold">${tablou.totalAplicari}</div>
        </div></div>
    </div>
    <div class="col-6 col-md-3">
        <div class="card shadow-sm h-100"><div class="card-body">
            <div class="text-muted small">Cu interviu programat</div>
            <div class="fs-3 fw-semibold text-warning">${tablou.aplicariCuInterviuProgramat}</div>
        </div></div>
    </div>
    <div class="col-6 col-md-3">
        <div class="card shadow-sm h-100"><div class="card-body">
            <div class="text-muted small">Oferte</div>
            <div class="fs-3 fw-semibold text-success">${tablou.oferte}</div>
        </div></div>
    </div>
    <div class="col-6 col-md-3">
        <div class="card shadow-sm h-100"><div class="card-body">
            <div class="text-muted small">Necesită follow-up</div>
            <div class="fs-3 fw-semibold text-danger">${tablou.followUpNecesar}</div>
        </div></div>
    </div>
</div>

<!-- Coloanele Kanban: câte una pentru fiecare status -->
<div class="d-flex gap-3 overflow-auto pb-3">
    <c:forEach var="coloana" items="${tablou.coloane}">
        <c:set var="status" value="${coloana.key}"/>
        <div class="flex-shrink-0" style="width: 270px;">
            <div class="d-flex justify-content-between align-items-center mb-2 px-1">
                <span class="badge text-bg-${status.culoare}">${status.eticheta}</span>
                <span class="text-muted small">${fn:length(coloana.value)}</span>
            </div>

            <div class="bg-body-secondary rounded p-2" style="min-height: 120px;">
                <c:forEach var="card" items="${coloana.value}">
                    <div class="card shadow-sm mb-2">
                        <div class="card-body p-2">

                            <!-- SQL Server: poziția -->
                            <a href="${pageContext.request.contextPath}/aplicari/detalii?id=${card.aplicare.id}"
                               class="fw-semibold text-decoration-none d-block">
                                <c:out value="${card.aplicare.pozitie}"/>
                            </a>

                            <!-- PostgreSQL: compania -->
                            <div class="small text-muted">
                                <i class="bi bi-building"></i>
                                <c:out value="${card.companie.nume}" default="companie inexistentă"/>
                            </div>

                            <!-- SQL Server: zilele de la aplicare -->
                           <div class="small text-muted">
                               <i class="bi bi-clock"></i>
                               <c:choose>
                                   <c:when test="${card.zileDeLaAplicare == 0}">azi</c:when>
                                   <c:when test="${card.zileDeLaAplicare == 1}">ieri</c:when>
                                   <c:otherwise>acum ${card.zileDeLaAplicare} zile</c:otherwise>
                               </c:choose>
                           </div>

                            <!-- Oracle: următorul interviu -->
                            <c:if test="${not empty card.urmatorulInterviu}">
                                <div class="small mt-1">
                                    <span class="badge text-bg-warning">
                                        <i class="bi bi-calendar-event"></i>
                                        ${card.urmatorulInterviu.dataOraText} · ${card.urmatorulInterviu.tip.eticheta}
                                    </span>
                                </div>
                            </c:if>

                            <!-- Alerta de follow-up -->
                            <c:if test="${card.necesitaFollowUp}">
                                <div class="small mt-1">
                                    <span class="badge text-bg-danger"><i class="bi bi-envelope"></i> Trimite follow-up</span>
                                </div>
                            </c:if>

                            <!-- Schimbarea statusului direct de pe card -->
                            <form method="post" action="${pageContext.request.contextPath}/tablou/status" class="mt-2">
                                <input type="hidden" name="aplicareId" value="${card.aplicare.id}">
                                <select name="status" class="form-select form-select-sm" onchange="this.form.submit()">
                                    <c:forEach var="s" items="${statusuri}">
                                        <option value="${s}" ${s == status ? 'selected' : ''}>${s.eticheta}</option>
                                    </c:forEach>
                                </select>
                            </form>
                        </div>
                    </div>
                </c:forEach>

                <c:if test="${empty coloana.value}">
                    <div class="text-center text-muted small py-3">Nicio aplicare</div>
                </c:if>
            </div>
        </div>
    </c:forEach>
</div>

<%@ include file="/WEB-INF/views/fragmente/subsol.jspf" %>