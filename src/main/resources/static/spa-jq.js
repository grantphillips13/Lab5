// Minimal jQuery SPA that mirrors the Spring tutorial, adapted to your endpoints.
$(function () {
    const api = '/api/addressbooks';

    const $createForm   = $('#createBookForm');
    const $addBuddyForm = $('#addBuddyForm');
    const $bookIdInput  = $('#bookId');
    const $listBtn      = $('#listBuddiesBtn');
    const $output       = $('#output');

    function line(msg, cls = '') {
        const $d = $('<div>').text(msg);
        if (cls) $d.addClass(cls);
        $output.prepend($d);
    }

    function parseIdFromLocation(locationHeader) {
        if (!locationHeader) return null;
        try {
            const parts = locationHeader.split('/');
            return parts[parts.length - 1];
        } catch {
            return null;
        }
    }

    // Create address book (POST /api/addressbooks)
    $createForm.on('submit', function (e) {
        e.preventDefault();
        $.ajax({
            url: api,
            method: 'POST',
            success: (_data, _status, xhr) => {
                const loc = xhr.getResponseHeader('Location');
                const id  = parseIdFromLocation(loc);
                if (id) {
                    $bookIdInput.val(id);
                    line(`Created address book → id=${id} (Location: ${loc})`, 'ok');
                } else {
                    line(`Created book but couldn't read Location header.`, 'error');
                }
            },
            error: (xhr) => line(`Create failed: HTTP ${xhr.status}`, 'error')
        });
    });

    // Add buddy (POST /api/addressbooks/{id}/buddies)
    $addBuddyForm.on('submit', function (e) {
        e.preventDefault();
        const id = $bookIdInput.val();
        if (!id) return line('Please create or enter an AddressBook ID first.', 'error');

        const payload = {
            name: $('#name').val(),
            phoneNumber: $('#phone').val(),
            address: $('#address').val()
        };

        $.ajax({
            url: `${api}/${id}/buddies`,
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(payload),
            success: (data, _status, xhr) => {
                const loc = xhr.getResponseHeader('Location') || '(no Location)';
                line(`Added buddy: ${data.name} (${data.phoneNumber}) – ${data.address} → ${loc}`, 'ok');
            },
            error: (xhr) => line(`Add failed: HTTP ${xhr.status}`, 'error')
        });
    });

    // List buddies (GET /api/addressbooks/{id}/buddies)
    $listBtn.on('click', function () {
        const id = $bookIdInput.val();
        if (!id) return line('Please create or enter an AddressBook ID first.', 'error');

        $.getJSON(`${api}/${id}/buddies`, function (buddies) {
            if (!Array.isArray(buddies) || buddies.length === 0) {
                line('No buddies yet for this address book.');
                return;
            }
            const lines = buddies.map(b => `• ${b.name} (${b.phoneNumber}) – ${b.address}`);
            line(lines.join('\n'));
        }).fail(xhr => line(`List failed: HTTP ${xhr.status}`, 'error'));
    });
});
