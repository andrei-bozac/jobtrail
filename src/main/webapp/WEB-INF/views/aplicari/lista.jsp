<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="titluPagina" value="Aplicări" scope="request"/>
<c:set var="meniuActiv" value="aplicari" scope="request"/>
<%@ include file="/WEB-INF/views/fragmente/antet.jspf" %>

<div class="d-flex justify-content-between align-items-center mb-3">
    <div>
        <h1 class="h3 mb-0">Aplicări</h1>
        <p class="text-muted mb-0">
            ${fn:length(aplicari)} aplicări
            <c:if test="${not empty filtruStatus}"> cu statusul „${filtruStatus.eticheta}”</c:if>
        </p>
    </div>
    <a href="${pageContext.request.contextPath}/aplicari/nou" class="btn btn-primary">
        <i class="bi bi-plus-lg"></i> Aplicare nouă
    </a>
</div>

<div class="mb-3 d-flex flex-wrap gap-2">
    <a href="${pageContext.request.contextPath}/aplicari"
       class="btn btn-sm ${empty filtruStatus ? 'btn-dark' : 'btn-outline-dark'}">Toate</a>
    <c:forEach var="s" items="${statusuri}">
        <a href="${pageContext.request.contextPath}/aplicari?status=${s}"
           class="btn btn-sm ${s == filtruStatus ? 'btn-' : 'btn-outline-'}${s.culoare}">${s.eticheta}</a>
    </c:forEach>
</div>

<c:choose>
    <c:when test="${empty aplicari}">
        <div class="alert alert-info">Nu există aplicări pentru filtrul ales.</div>
    </c:when>
    <c:otherwise>
        <div class="card shadow-sm">
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead class="table-light">
                    <tr>
                        <th>Poziție</th>
                        <th>Companie</th>
                        <th>Status</th>
                        <th>Tip</th>
                        <th>Mod de lucru</th>
                        <th>Data aplicării</th>
                        <th>Salariu</th>
                        <th class="text-end">Acțiuni</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="a" items="${aplicari}">
                        <%-- Compania vine din PostgreSQL: o căutăm în hartă după ID --%>
                        <c:set var="companie" value="${companii[a.companieId]}"/>
                        <tr>
                            <td class="fw-semibold">
                                <a href="${pageContext.request.contextPath}/aplicari/detalii?id=${a.id}"
                                   class="text-decoration-none"><c:out value="${a.pozitie}"/></a>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty companie}">
                                        <a href="${pageContext.request.contextPath}/companii/detalii?id=${companie.id}"
                                           class="text-decoration-none text-body"><c:out value="${companie.nume}"/></a>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-danger">companie inexistentă</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td><span class="badge text-bg-${a.status.culoare}">${a.status.eticheta}</span></td>
                            <td>${a.tip.eticheta}</td>
                            <td>${empty a.modLucru ? '—' : a.modLucru.eticheta}</td>
                            <td>${a.dataAplicarii}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty a.salariu}">${a.salariu} lei</c:when>
                                    <c:otherwise>—</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-end text-nowrap">
                                <a href="${pageContext.request.contextPath}/aplicari/detalii?id=${a.id}"
                                   class="btn btn-sm btn-outline-secondary" title="Detalii">
                                    <i class="bi bi-eye"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/aplicari/editare?id=${a.id}"
                                   class="btn btn-sm btn-outline-primary" title="Editează">
                                    <i class="bi bi-pencil"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/fragmente/subsol.jspf" %>