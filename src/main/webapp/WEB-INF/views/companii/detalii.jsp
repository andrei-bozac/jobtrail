<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="titluPagina" value="${companie.nume}" scope="request"/>
<c:set var="meniuActiv" value="companii" scope="request"/>
<%@ include file="/WEB-INF/views/fragmente/antet.jspf" %>

<a href="${pageContext.request.contextPath}/companii" class="text-decoration-none small">
    <i class="bi bi-arrow-left"></i> Înapoi la companii
</a>

<!-- Titlul și butoanele -->
<div class="d-flex justify-content-between align-items-center mt-2 mb-3">
    <h1 class="h3 mb-0"><c:out value="${companie.nume}"/></h1>
    <div class="d-flex gap-2">
        <a href="${pageContext.request.contextPath}/companii/editare?id=${companie.id}" class="btn btn-outline-primary">
            <i class="bi bi-pencil"></i> Editează
        </a>
        <form method="post" action="${pageContext.request.contextPath}/companii/sterge"
              onsubmit="return confirm('Sigur vrei să ștergi compania? Vor fi șterse și toate contactele ei.');">
            <input type="hidden" name="id" value="${companie.id}">
            <button type="submit" class="btn btn-outline-danger">
                <i class="bi bi-trash"></i> Șterge
            </button>
        </form>
    </div>
</div>

<!-- Informațiile companiei (PostgreSQL) -->
<div class="card shadow-sm mb-4">
    <div class="card-body">
        <dl class="row mb-0">
            <dt class="col-sm-3">Domeniu</dt>
            <dd class="col-sm-9"><c:out value="${companie.domeniu}" default="—"/></dd>

            <dt class="col-sm-3">Oraș</dt>
            <dd class="col-sm-9"><c:out value="${companie.oras}" default="—"/></dd>

            <dt class="col-sm-3">Dimensiune</dt>
            <dd class="col-sm-9">
                <c:choose>
                    <c:when test="${not empty companie.dimensiune}">
                        <span class="badge text-bg-secondary">${companie.dimensiune}</span>
                    </c:when>
                    <c:otherwise>—</c:otherwise>
                </c:choose>
            </dd>

            <dt class="col-sm-3">Website</dt>
            <dd class="col-sm-9">
                <c:choose>
                    <c:when test="${not empty companie.website}">
                        <a href="${fn:escapeXml(companie.website)}" target="_blank" rel="noopener">
                            <c:out value="${companie.website}"/>
                        </a>
                    </c:when>
                    <c:otherwise>—</c:otherwise>
                </c:choose>
            </dd>

            <dt class="col-sm-3">Note</dt>
            <dd class="col-sm-9 mb-0" style="white-space: pre-line;"><c:out value="${companie.note}" default="—"/></dd>
        </dl>
    </div>
</div>

<!-- Contactele companiei (PostgreSQL) -->
<div class="card shadow-sm">
    <div class="card-header bg-white d-flex justify-content-between align-items-center">
        <h2 class="h5 mb-0">Contacte <span class="text-muted">(${fn:length(contacte)})</span></h2>
        <a href="${pageContext.request.contextPath}/contacte/nou?companieId=${companie.id}" class="btn btn-sm btn-primary">
            <i class="bi bi-person-plus"></i> Adaugă contact
        </a>
    </div>

    <c:choose>
        <c:when test="${empty contacte}">
            <div class="card-body text-muted">Nu există contacte pentru această companie.</div>
        </c:when>
        <c:otherwise>
            <div class="table-responsive">
                <table class="table align-middle mb-0">
                    <thead class="table-light">
                    <tr>
                        <th>Nume</th>
                        <th>Funcție</th>
                        <th>Email</th>
                        <th>Telefon</th>
                        <th>LinkedIn</th>
                        <th class="text-end">Acțiuni</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="ct" items="${contacte}">
                        <tr>
                            <td class="fw-semibold"><c:out value="${ct.nume}"/></td>
                            <td><c:out value="${ct.functie}" default="—"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty ct.email}">
                                        <a href="mailto:${fn:escapeXml(ct.email)}"><c:out value="${ct.email}"/></a>
                                    </c:when>
                                    <c:otherwise>—</c:otherwise>
                                </c:choose>
                            </td>
                            <td><c:out value="${ct.telefon}" default="—"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty ct.linkedin}">
                                        <a href="${fn:escapeXml(ct.linkedin)}" target="_blank" rel="noopener">
                                            <i class="bi bi-linkedin"></i> profil
                                        </a>
                                    </c:when>
                                    <c:otherwise>—</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-end text-nowrap">
                                <a href="${pageContext.request.contextPath}/contacte/editare?id=${ct.id}"
                                   class="btn btn-sm btn-outline-primary" title="Editează">
                                    <i class="bi bi-pencil"></i>
                                </a>
                                <form method="post" action="${pageContext.request.contextPath}/contacte/sterge"
                                      class="d-inline"
                                      onsubmit="return confirm('Sigur vrei să ștergi acest contact?');">
                                    <input type="hidden" name="id" value="${ct.id}">
                                    <button type="submit" class="btn btn-sm btn-outline-danger" title="Șterge">
                                        <i class="bi bi-trash"></i>
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<!-- Aplicările la această companie (SQL Server) -->
<div class="card shadow-sm mt-4">
    <div class="card-header bg-white d-flex justify-content-between align-items-center">
        <h2 class="h5 mb-0">Aplicări <span class="text-muted">(${fn:length(aplicari)})</span></h2>
        <a href="${pageContext.request.contextPath}/aplicari/nou?companieId=${companie.id}" class="btn btn-sm btn-primary">
            <i class="bi bi-plus-lg"></i> Aplicare nouă
        </a>
    </div>
    <c:choose>
        <c:when test="${empty aplicari}">
            <div class="card-body text-muted">Nu ai aplicat încă la această companie.</div>
        </c:when>
        <c:otherwise>
            <ul class="list-group list-group-flush">
                <c:forEach var="a" items="${aplicari}">
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        <a href="${pageContext.request.contextPath}/aplicari/detalii?id=${a.id}" class="text-decoration-none">
                            <c:out value="${a.pozitie}"/>
                        </a>
                        <span>
                            <span class="text-muted small me-2">${a.dataAplicarii}</span>
                            <span class="badge text-bg-${a.status.culoare}">${a.status.eticheta}</span>
                        </span>
                    </li>
                </c:forEach>
            </ul>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/WEB-INF/views/fragmente/subsol.jspf" %>