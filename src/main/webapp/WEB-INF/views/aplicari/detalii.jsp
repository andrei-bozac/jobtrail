<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="titluPagina" value="${aplicare.pozitie}" scope="request"/>
<c:set var="meniuActiv" value="aplicari" scope="request"/>
<%@ include file="/WEB-INF/views/fragmente/antet.jspf" %>

<a href="${pageContext.request.contextPath}/aplicari" class="text-decoration-none small">
    <i class="bi bi-arrow-left"></i> Înapoi la aplicări
</a>

<div class="d-flex justify-content-between align-items-start mt-2 mb-3">
    <div>
        <h1 class="h3 mb-1"><c:out value="${aplicare.pozitie}"/></h1>
        <c:choose>
            <c:when test="${not empty companie}">
                <a href="${pageContext.request.contextPath}/companii/detalii?id=${companie.id}" class="text-decoration-none">
                    <i class="bi bi-building"></i> <c:out value="${companie.nume}"/>
                </a>
            </c:when>
            <c:otherwise><span class="text-danger">companie inexistentă</span></c:otherwise>
        </c:choose>
    </div>
    <div class="d-flex gap-2">
        <a href="${pageContext.request.contextPath}/aplicari/editare?id=${aplicare.id}" class="btn btn-outline-primary">
            <i class="bi bi-pencil"></i> Editează
        </a>
        <form method="post" action="${pageContext.request.contextPath}/aplicari/sterge"
              onsubmit="return confirm('Sigur vrei să ștergi această aplicare?');">
            <input type="hidden" name="id" value="${aplicare.id}">
            <button type="submit" class="btn btn-outline-danger"><i class="bi bi-trash"></i> Șterge</button>
        </form>
    </div>
</div>

<div class="card shadow-sm mb-4">
    <div class="card-body">
        <dl class="row mb-0">
            <dt class="col-sm-3">Status</dt>
            <dd class="col-sm-9"><span class="badge text-bg-${aplicare.status.culoare}">${aplicare.status.eticheta}</span></dd>

            <dt class="col-sm-3">Tip</dt>
            <dd class="col-sm-9">${aplicare.tip.eticheta}</dd>

            <dt class="col-sm-3">Mod de lucru</dt>
            <dd class="col-sm-9">${empty aplicare.modLucru ? '—' : aplicare.modLucru.eticheta}</dd>

            <dt class="col-sm-3">Sursă</dt>
            <dd class="col-sm-9">${aplicare.sursa.eticheta}</dd>

            <dt class="col-sm-3">Data aplicării</dt>
            <dd class="col-sm-9">${aplicare.dataAplicarii}</dd>

            <dt class="col-sm-3">Salariu</dt>
            <dd class="col-sm-9">
                <c:choose>
                    <c:when test="${not empty aplicare.salariu}">${aplicare.salariu} lei</c:when>
                    <c:otherwise>—</c:otherwise>
                </c:choose>
            </dd>

            <dt class="col-sm-3">Anunț</dt>
            <dd class="col-sm-9">
                <c:choose>
                    <c:when test="${not empty aplicare.linkAnunt}">
                        <a href="${fn:escapeXml(aplicare.linkAnunt)}" target="_blank" rel="noopener">deschide anunțul</a>
                    </c:when>
                    <c:otherwise>—</c:otherwise>
                </c:choose>
            </dd>

            <dt class="col-sm-3">Note</dt>
            <dd class="col-sm-9 mb-0" style="white-space: pre-line;"><c:out value="${aplicare.note}" default="—"/></dd>
        </dl>
    </div>
</div>

<c:set var="aplicareInchisa" value="${aplicare.status == 'RESPINS' or aplicare.status == 'RETRAS'}"/>
<div class="card shadow-sm">
    <div class="card-header bg-white d-flex justify-content-between align-items-center">
        <h2 class="h5 mb-0">Interviuri <span class="text-muted">(${fn:length(interviuri)})</span></h2>
        <c:choose>
            <c:when test="${aplicareInchisa}">
                <span class="small text-muted">Aplicarea este încheiată</span>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/interviuri/nou?aplicareId=${aplicare.id}" class="btn btn-sm btn-primary">
                    <i class="bi bi-plus-lg"></i> Adaugă interviu
                </a>
            </c:otherwise>
        </c:choose>
    </div>
    <c:choose>
        <c:when test="${empty interviuri}">
            <div class="card-body text-muted">Nu există interviuri pentru această aplicare.</div>
        </c:when>
        <c:otherwise>
            <ul class="list-group list-group-flush">
                <c:forEach var="i" items="${interviuri}">
                    <li class="list-group-item">
                        <div class="d-flex justify-content-between align-items-start">
                            <div>
                                <span class="fw-semibold">${i.dataOraText}</span>
                                · Interviu ${i.tip.eticheta}
                                <span class="badge text-bg-${i.rezultat.culoare} ms-1">${i.rezultat.eticheta}</span>
                                <c:if test="${i.viitor}"><span class="badge text-bg-info ms-1">urmează</span></c:if>
                                <c:if test="${not empty i.intervievator}">
                                    <div class="small text-muted">cu <c:out value="${i.intervievator}"/></div>
                                </c:if>
                            </div>
                            <div class="text-nowrap">
                                <a href="${pageContext.request.contextPath}/interviuri/editare?id=${i.id}"
                                   class="btn btn-sm btn-outline-primary" title="Editează"><i class="bi bi-pencil"></i></a>
                                <form method="post" action="${pageContext.request.contextPath}/interviuri/sterge" class="d-inline"
                                      onsubmit="return confirm('Sigur vrei să ștergi acest interviu?');">
                                    <input type="hidden" name="id" value="${i.id}">
                                    <button type="submit" class="btn btn-sm btn-outline-danger" title="Șterge"><i class="bi bi-trash"></i></button>
                                </form>
                            </div>
                        </div>
                        <c:if test="${not empty i.intrebari}">
                            <div class="small mt-2" style="white-space: pre-line;"><strong>Întrebări:</strong> <c:out value="${i.intrebari}"/></div>
                        </c:if>
                        <c:if test="${not empty i.impresii}">
                            <div class="small mt-1" style="white-space: pre-line;"><strong>Impresii:</strong> <c:out value="${i.impresii}"/></div>
                        </c:if>
                    </li>
                </c:forEach>
            </ul>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/WEB-INF/views/fragmente/subsol.jspf" %>