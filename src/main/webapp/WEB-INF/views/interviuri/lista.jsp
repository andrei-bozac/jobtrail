<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="titluPagina" value="Interviuri" scope="request"/>
<c:set var="meniuActiv" value="interviuri" scope="request"/>
<%@ include file="/WEB-INF/views/fragmente/antet.jspf" %>

<h1 class="h3 mb-3">Interviuri</h1>


<h2 class="h5 mb-2"><i class="bi bi-calendar-event"></i> Următoarele interviuri (${fn:length(viitoare)})</h2>
<c:choose>
    <c:when test="${empty viitoare}">
        <div class="alert alert-light border mb-4">Nu ai niciun interviu programat.</div>
    </c:when>
    <c:otherwise>
        <div class="row g-3 mb-4">
            <c:forEach var="i" items="${viitoare}">
                <c:set var="a" value="${aplicari[i.aplicareId]}"/>
                <div class="col-md-4">
                    <div class="card shadow-sm h-100 border-warning">
                        <div class="card-body">
                            <div class="fw-semibold">${i.dataOraText}</div>
                            <div class="small text-muted mb-2">Interviu ${i.tip.eticheta}</div>
                            <a href="${pageContext.request.contextPath}/aplicari/detalii?id=${i.aplicareId}" class="text-decoration-none">
                                <c:out value="${a.pozitie}"/>
                            </a>
                            <div class="small"><c:out value="${companii[a.companieId].nume}"/></div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>

<!-- Toate interviurile -->
<h2 class="h5 mb-2">Toate interviurile (${fn:length(interviuri)})</h2>
<div class="card shadow-sm">
    <div class="table-responsive">
        <table class="table table-hover align-middle mb-0">
            <thead class="table-light">
            <tr>
                <th>Data și ora</th>
                <th>Tip</th>
                <th>Rezultat</th>
                <th>Poziție</th>
                <th>Companie</th>
                <th>Intervievator</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="i" items="${interviuri}">
                <c:set var="a" value="${aplicari[i.aplicareId]}"/>
                <tr>
                    <td>${i.dataOraText}</td>
                    <td>${i.tip.eticheta}</td>
                    <td><span class="badge text-bg-${i.rezultat.culoare}">${i.rezultat.eticheta}</span></td>
                    <td>
                        <a href="${pageContext.request.contextPath}/aplicari/detalii?id=${i.aplicareId}" class="text-decoration-none">
                            <c:out value="${a.pozitie}" default="(aplicare inexistentă)"/>
                        </a>
                    </td>
                    <td><c:out value="${companii[a.companieId].nume}" default="—"/></td>
                    <td><c:out value="${i.intervievator}" default="—"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="/WEB-INF/views/fragmente/subsol.jspf" %>